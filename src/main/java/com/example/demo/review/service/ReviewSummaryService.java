package com.example.demo.review.service;

import com.example.demo.common.exception.PropertyNotFoundException;
import com.example.demo.property.domain.Property;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.review.dto.Review.Response.AiSummaryResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Iterator;

@Slf4j
@Service
@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "RestTemplate and ObjectMapper are safely copied defensively in constructor"
)
public class ReviewSummaryService {

    private final PropertyRepository propertyRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ReviewSummaryService(PropertyRepository propertyRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.propertyRepository = propertyRepository;
        this.restTemplate = new RestTemplate(restTemplate.getRequestFactory()); // defensive copy
        this.objectMapper = objectMapper.copy(); // defensive copy
    }

    public AiSummaryResponse getSummaryFromAi(Long propertyId) {
        log.info("[요약 요청 시작] propertyId: {}", propertyId);

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> {
                    log.warn("[매물 없음] propertyId: {}", propertyId);
                    return new PropertyNotFoundException();
                });

        String district = property.getDivisionName();
        String articleNo = property.getArticleNo();

        log.info("[매물 정보 조회 성공] district: {}, articleNo: {}", district, articleNo);

        return fetchFromAi(district, articleNo);
    }

    private AiSummaryResponse fetchFromAi(String district, String articleNo) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl("http://1.230.77.225:8000")
                .pathSegment("metajson", district, "summaries")
                .build()
                .encode()
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

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            if (root == null || root.isNull()) {
                log.error("[AI 응답 파싱 실패] root is null");
                throw new RuntimeException("AI 응답 파싱 실패: root가 null입니다.");
            }

            JsonNode summaries = root.path("summaries");
            if (summaries == null || summaries.isMissingNode() || summaries.isNull()) {
                log.error("[summaries 없음] 응답에 summaries 노드 없음");
                throw new RuntimeException("AI 응답에 summaries 노드가 없습니다.");
            }

            for (Iterator<String> it = summaries.fieldNames(); it.hasNext(); ) {
                String key = it.next();
                JsonNode item = summaries.get(key);
                String responseArticleNo = item.path("articleNo").asText();

                log.debug("[응답 요약 비교] 응답 articleNo: {}", responseArticleNo);

                if (articleNo.equals(responseArticleNo)) {
                    return extractSummary(item, articleNo);
                }
            }

            log.warn("[요약 미존재] articleNo: {}", articleNo);
            throw new RuntimeException("해당 articleNo에 대한 AI 요약 없음");

        } catch (JsonProcessingException e) {
            log.error("[요약 파싱 실패] articleNo: {}, 에러: {}", articleNo, e.getMessage(), e);
            throw new RuntimeException("AI 응답 파싱 실패", e);
        }
    }


    private AiSummaryResponse extractSummary(JsonNode item, String articleNo) {
        JsonNode summaryNode = item.get("summary");

        if (summaryNode == null || summaryNode.isNull()) {
            log.warn("[요약 없음] articleNo: {}", articleNo);
            throw new RuntimeException("요약 내용이 없습니다.");
        }

        try {
            AiSummaryResponse result = objectMapper.treeToValue(summaryNode, AiSummaryResponse.class);
            log.info("[요약 파싱 성공] articleNo: {}", articleNo);
            return result;
        } catch (JsonProcessingException e) {
            log.error("[요약 변환 실패] articleNo: {}, 에러: {}", articleNo, e.getMessage(), e);
            throw new RuntimeException("요약 변환 중 오류 발생", e);
        }
    }
}
