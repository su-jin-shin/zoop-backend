package com.example.demo.review.service;

import com.example.demo.common.exception.PropertyNotFoundException;
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
import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;

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

    // ================ 의존성 주입 ==============================
    private final PropertyRepository propertyRepository;            // 매물 정보 접근용
    private final ReviewSummaryRepository reviewSummaryRepository;  // 리뷰 요약 저장
    private final RestTemplate restTemplate;                        // 외부 AI 서버 요청용
    private final ObjectMapper objectMapper;                        // JSON 파싱용 Jackson

    // ========== 요약 조회 or 가져오기 ===========================
    /*
     * 해당 propertyId의 요약 정보를 DB에서 조회 (ReviewSummary 테이블)
     * 없으면 AI 서버에서 가져와 저장 후 반환
     */
    public AiSummaryResponse getOrFetchSummary(Long propertyId) {
        List<ReviewSummary> existing = reviewSummaryRepository.findByPropertyId(propertyId);

        // 기존에 저장된 요약 정보가 있으면 캐시처럼 사용
        if (!existing.isEmpty()) {
            log.info("[요약 캐시 사용] propertyId: {}", propertyId);
            return toDto(existing);
        }

        // 없으면 AI 서버에 요청해서 가져오기
        AiSummaryResponse fetched = fetchFromAi(propertyId);

        // DB 저장
        saveSummary(propertyId, fetched);
        return fetched;
    }

    // ============= AI 서버 요청 ============================

    private AiSummaryResponse fetchFromAi(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(PropertyNotFoundException::new);

        String district = property.getDivisionName();      // 지역구 이름(예 : "강남구")
        String articleNo = property.getArticleNo();       // 매물 번호

        // AI 서버 URI 구성하기
        URI uri = UriComponentsBuilder
                .fromHttpUrl("http://1.230.77.225:8000")  // 외부 AI 서버 주소
                .pathSegment("metajson", district, "summaries")  //경로 : /metajson/{district}/summaries
                .build()
                .encode()
                .toUri();

        log.info("[AI 요약 요청] URI: {}, articleNo: {}", uri, articleNo);

        // HTTP GET REQUEST
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        // 응답 유효성 검사
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("AI 서버 응답 실패");
        }

        try {
            // 응답 JSON 파싱
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode summaries = root.path("summaries");

            // summaries 안에서 articleNo와 일치하는 항목 찾기
            for (Iterator<String> it = summaries.fieldNames(); it.hasNext(); ) {
                JsonNode item = summaries.get(it.next());
                if (articleNo.equals(item.path("articleNo").asText())) {
                    JsonNode summaryNode = item.path("summary");

                    if (summaryNode == null || summaryNode.isNull()) {
                        throw new RuntimeException("요약 내용이 없습니다");
                    }

                    // JSON TO DTO(변환)
                    return objectMapper.treeToValue(summaryNode, AiSummaryResponse.class);
                }
            }
            throw new RuntimeException("해당 매물에 대한 AI 요약 없음");

        } catch (JsonProcessingException e) {
            throw new RuntimeException("AI 응답 파싱 실패", e);
        }
    }

    //=================응답받은 요약 저장 ================

    public void saveSummary(Long propertyId, AiSummaryResponse dto) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException());

        Complex complex = property.getComplex(); // 매물의 단지 정보

        // 이미 저장된 기록이 있으면 중복저장 방지
        if (complex != null && reviewSummaryRepository.existsByComplexId(complex.getId())) {
            log.info("[중복 저장 차단] complexId={} 이미 저장됨", complex.getId());
            return;
        }

        // 요약 타입별 리스트 맵 구성
        Map<SummaryType, List<String>> summaryMap = Map.of(
                SummaryType.GOOD, dto.getGood(),
                SummaryType.BAD, dto.getBad(),
                SummaryType.TRA, dto.getTra(),
                SummaryType.EDU, dto.getEdu(),
                SummaryType.HEL, dto.getHel(),
                SummaryType.LOC, dto.getLoc()
        );

        // 각 요약 항목을 ReviewSummary 엔티티로 저장
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
    //================== 엔티티 --> DTO 변환 =========
    /*
    * DB에서 조회된 ReviewSummary 목록을 AiSummaryResponse(DTO) 형태로 반환
     */

    private AiSummaryResponse toDto(List<ReviewSummary> summaries) {
        // SummaryType 기준으로 그룹화
        Map<SummaryType, List<String>> grouped = summaries.stream()
                .collect(Collectors.groupingBy(
                        ReviewSummary::getSummaryType,
                        Collectors.mapping(ReviewSummary::getContent, Collectors.toList())
                ));

        // DTO 구성
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


