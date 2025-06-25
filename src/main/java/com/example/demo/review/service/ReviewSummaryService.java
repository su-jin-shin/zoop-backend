package com.example.demo.review.service;

import com.example.demo.common.exception.PropertyNotFoundException;
import com.example.demo.property.domain.Property;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.review.dto.Review.Response.AiSummaryResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewSummaryService {

    private final PropertyRepository propertyRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AiSummaryResponse getSummaryFromAi(Long propertyId) {
        log.info("[요약 요청 시작] propertyId: {}", propertyId);

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> {
                    log.warn("[매물 없음] propertyId: {}", propertyId);
                    return new PropertyNotFoundException();
                });

        String articleNo = property.getArticleNo();
        String address = property.getCityName() + " " + property.getDivisionName() + " " + property.getSectionName();

        log.info("[매물 정보 조회 성공] address: {}, articleNo: {}", address, articleNo);

        return fetchFromAi(property, articleNo);
    }

    private AiSummaryResponse fetchFromAi(Property property, String articleNo) {
        String fullAddress = property.getCityName() + " " +
                property.getDivisionName() + " " +
                property.getSectionName();

        URI uri = UriComponentsBuilder
                .fromHttpUrl("http://1.230.77.225:8000")
                .pathSegment("metajson", fullAddress, "summaries")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        log.info("[AI 요약 요청 전송] URI: {}, articleNo: {}", uri, articleNo);

        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(uri, String.class);
        } catch (Exception e) {
            log.error("[AI 서버 요청 실패] URI: {}, 에러: {}", uri, e.getMessage(), e);
            throw new RuntimeException("AI 서버 요청 실패", e);
        }

        String responseBody = response.getBody();
        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null) {
            log.error("[AI 응답 오류] status: {}, body: {}", response.getStatusCode(), responseBody);
            throw new RuntimeException("AI 서버 응답 실패");
        }

        log.info("[AI 응답 수신 완료] status: {}, body length: {}", response.getStatusCode(), responseBody.length());
        log.debug("[AI 응답 본문] {}", responseBody);

        return parseSummaryResponse(responseBody, articleNo);
    }

    private AiSummaryResponse parseSummaryResponse(String responseBody, String articleNo) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode summaries = root.path("summaries");

            if (summaries.isMissingNode() || !summaries.isObject()) {
                log.error("[summaries 필드 없음 또는 잘못된 구조] summaries: {}", summaries);
                throw new RuntimeException("summaries 필드가 없거나 잘못된 구조입니다.");
            }

            for (Iterator<String> it = summaries.fieldNames(); it.hasNext(); ) {
                String key = it.next();
                JsonNode item = summaries.get(key);

                String responseArticleNo = item.path("articleNo").asText();
                log.debug("[응답 요약 비교] 응답 articleNo: {}, 비교 대상: {}", responseArticleNo, articleNo);

                if (articleNo.equals(responseArticleNo)) {
                    return extractSummary(item, articleNo);
                }
            }

            log.warn("[요약 미존재] articleNo: {}", articleNo);
            throw new RuntimeException("해당 articleNo에 대한 요약 정보가 없습니다.");

        } catch (JsonProcessingException e) {
            log.error("[요약 파싱 실패] articleNo: {}, 에러: {}", articleNo, e.getMessage(), e);
            throw new RuntimeException("AI 응답 파싱 중 오류 발생", e);
        }
    }

    private AiSummaryResponse extractSummary(JsonNode item, String articleNo) {
        JsonNode summaryNode = item.path("summary");

        if (summaryNode == null || summaryNode.isNull()) {
            log.warn("[요약 없음] articleNo: {}", articleNo);
            throw new RuntimeException("요약 데이터가 존재하지 않습니다.");
        }

        try {
            AiSummaryResponse summary = objectMapper.treeToValue(summaryNode, AiSummaryResponse.class);
            log.info("[요약 파싱 성공] articleNo: {}", articleNo);
            return summary;
        } catch (JsonProcessingException e) {
            log.error("[요약 변환 실패] articleNo: {}, 에러: {}", articleNo, e.getMessage(), e);
            throw new RuntimeException("요약 객체 변환 실패", e);
        }
    }
}
