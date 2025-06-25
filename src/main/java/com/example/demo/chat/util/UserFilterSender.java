package com.example.demo.chat.util;

import com.example.demo.Filter.dto.request.RefinedFilterDto;
import com.example.demo.chat.constants.Constants;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.property.dto.RecommendedPropertyDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class UserFilterSender {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<RecommendedPropertyDto> send(RefinedFilterDto filters) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RefinedFilterDto> request = new HttpEntity<>(filters, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(Constants.CRAWL_AND_RECOMMEND_URL, request, String.class);

        String body = response.getBody();
        if (body == null || body.isBlank()) {
            throw new IllegalStateException("서버 응답이 비어 있습니다.");
        }

        JsonNode root = mapper.readTree(body);
        log.info("root: {}", root);
        JsonNode resultsNode = root.path("data").path("results");

        if (resultsNode == null || resultsNode.isMissingNode()) {
            throw new IllegalStateException("'data.results' 필드가 응답에 존재하지 않습니다.");
        }

        if (!resultsNode.isArray()) {
            throw new IllegalStateException("'data.results'는 배열이 아닙니다.");
        }

        List<RecommendedPropertyDto> resultList = new ArrayList<>();

        for (JsonNode node : resultsNode) {
            // summary → aiSummary 이름 변경
            JsonNode summaryNode = null;
            if (node.has("summary") && node instanceof ObjectNode objectNode) {
                summaryNode = objectNode.remove("summary");
                objectNode.set("aiSummary", summaryNode);
            }

            // articleNo 추출
            String articleNo = node.get("articleNo").asText();

            // articleNo 필드 제거 후 DTO 매핑할 JSON 만들기
            ((ObjectNode) node).remove("articleNo");

            // PropertyExcelDto로 역직렬화
            PropertyExcelDto propertyExcelDto = mapper.treeToValue(node, PropertyExcelDto.class);

            // summaryNode를 Map<String, List<String>> 형태로 파싱해서 RecommendedPropertyDto에 넣기
            Map<String, List<String>> aiSummaryMap = null;
            if (summaryNode != null) {
                aiSummaryMap = mapper.readValue(summaryNode.toString(), new TypeReference<>() {});
            }
            RecommendedPropertyDto recommended = new RecommendedPropertyDto(articleNo, propertyExcelDto, aiSummaryMap);

            // 리스트에 추가
            resultList.add(recommended);
        }

        return resultList;
    }

}
