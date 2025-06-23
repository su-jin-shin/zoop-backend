package com.example.demo.chat.util;

import com.example.demo.Filter.dto.request.RefinedFilterDto;
import com.example.demo.chat.constants.Constants;
import com.example.demo.common.excel.PropertyExcelDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


public class UserFilterSender {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<PropertyExcelDto> send(RefinedFilterDto filters) throws JsonProcessingException {
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
        JsonNode resultsNode = root.path("data").path("results");

        if (resultsNode == null || resultsNode.isMissingNode()) {
            throw new IllegalStateException("'data.results' 필드가 응답에 존재하지 않습니다.");
        }

        if (!resultsNode.isArray()) {
            throw new IllegalStateException("'data.results'는 배열이 아닙니다.");
        }

        // summary → aiSummary 이름 변경
        for (JsonNode node : resultsNode) {
            if (node.has("summary") && node instanceof ObjectNode objectNode) {
                JsonNode summaryNode = objectNode.remove("summary");
                objectNode.set("aiSummary", summaryNode);
            }
        }

        return mapper.readValue(resultsNode.toString(), new TypeReference<List<PropertyExcelDto>>() {});
    }

}
