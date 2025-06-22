package com.example.demo.chat.util;

//import com.example.demo.chat.constants.Constants;
//import com.example.demo.chat.dto.FilterDto;
//import com.example.demo.chat.dto.PropertyDto;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.*;
//import org.springframework.web.client.RestTemplate;

//import java.util.List;


//public class UserFilterSender {
//    private static final ObjectMapper mapper = new ObjectMapper();
//
//    public static List<PropertyDto> send(FilterDto filters) throws JsonProcessingException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<FilterDto> request = new HttpEntity<>(filters, headers);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.postForEntity(Constants.CRAWLING_API_URL, request, String.class);
//
//        JsonNode root = mapper.readTree(response.getBody()); // JSON 문자열을 JsonNode로 파싱
//        JsonNode dataNode = root.path("data");  // "data" 노드만 추출
//        return mapper.readValue(
//                dataNode.toString(),
//                new TypeReference<List<PropertyDto>>() {}
//        );
//    }
//}
