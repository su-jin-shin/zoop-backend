package com.example.demo.review.service;

import com.example.demo.common.exception.NotFoundException;
import com.example.demo.property.domain.Property;
import com.example.demo.property.repository.PropertyRepository;
import com.example.demo.review.domain.Complex;
import com.example.demo.review.domain.ReviewSummary;
import com.example.demo.review.domain.SummaryType;
import com.example.demo.review.dto.Review.AiSummaryResponse;
import com.example.demo.review.repository.ReviewSummaryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP2"},
        justification = "Spring DI로 주입된 객체는 변경 불가한 의도로 사용되므로 문제 없음"
)
public class ReviewSummaryService {

    private final PropertyRepository propertyRepository;
    private final ReviewSummaryRepository reviewSummaryRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AiSummaryResponse getOrFetchSummary(Long propertyId) {
        List<ReviewSummary> existing = reviewSummaryRepository.findByPropertyId(propertyId);
        if (!existing.isEmpty()) {
            log.info("[요약 캐시 사용] propertyId: {}", propertyId);
            return toDto(existing);
        }

        AiSummaryResponse fetched = fetchFromAi(propertyId);
        saveSummary(propertyId, fetched);
        return fetched;
    }

    private AiSummaryResponse fetchFromAi(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NotFoundException());

        String district = property.getDivisionName();
        String articleNo = property.getArticleNo();
        String url = "http://1.230.77.225:8000/metajson/" + district + "/summaries";

        log.info("[AI 요약 요청] URL: {}, articleNo: {}", url, articleNo);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("AI 서버 응답 실패");
        }

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode summaries = root.path("summaries");

            for (Iterator<String> it = summaries.fieldNames(); it.hasNext(); ) {
                JsonNode item = summaries.get(it.next());
                if (articleNo.equals(item.path("articleNo").asText())) {
                    JsonNode summaryNode = item.path("summary");
                    if (summaryNode == null || summaryNode.isNull()) {
                        throw new RuntimeException("요약 내용이 없습니다");
                    }
                    return objectMapper.treeToValue(summaryNode, AiSummaryResponse.class);
                }
            }

            throw new RuntimeException("해당 매물에 대한 AI 요약 없음");

        } catch (JsonProcessingException e) {
            throw new RuntimeException("AI 응답 파싱 실패", e);
        }
    }

    public void saveSummary(Long propertyId, AiSummaryResponse dto) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NotFoundException());
        Complex complex = property.getComplex();

        // ✅ 단지에 이미 저장된 기록이 있으면 저장 안 함
        if (complex != null && reviewSummaryRepository.existsByComplexId(complex.getId())) {
            log.info("[중복 저장 차단] complexId={} 이미 저장됨", complex.getId());
            return;
        }

        Map<SummaryType, List<String>> summaryMap = Map.of(
                SummaryType.GOOD, dto.getGood(),
                SummaryType.BAD, dto.getBad(),
                SummaryType.TRA, dto.getTra(),
                SummaryType.EDU, dto.getEdu(),
                SummaryType.HEL, dto.getHel(),
                SummaryType.LOC, dto.getLoc()
        );

        summaryMap.forEach((type, contents) -> {
            if (contents != null) {
                contents.forEach(content -> {
                    ReviewSummary summary = ReviewSummary.builder()
                            .complex(complex)
                            .propertyId(propertyId)
                            .summaryType(type)
                            .content(content)
                            .build();
                    reviewSummaryRepository.save(summary);
                });
            }
        });
    }

    private AiSummaryResponse toDto(List<ReviewSummary> summaries) {
        Map<SummaryType, List<String>> grouped = summaries.stream()
                .collect(Collectors.groupingBy(
                        ReviewSummary::getSummaryType,
                        Collectors.mapping(ReviewSummary::getContent, Collectors.toList())
                ));

        return AiSummaryResponse.builder()
                .good(grouped.getOrDefault(SummaryType.GOOD, List.of()))
                .bad(grouped.getOrDefault(SummaryType.BAD, List.of()))
                .tra(grouped.getOrDefault(SummaryType.TRA, List.of()))
                .edu(grouped.getOrDefault(SummaryType.EDU, List.of()))
                .hel(grouped.getOrDefault(SummaryType.HEL, List.of()))
                .loc(grouped.getOrDefault(SummaryType.LOC, List.of()))
                .build();
    }
}


