//package com.example.demo.mypage.controller;
//
//import com.example.demo.auth.dto.LoginUser;
//import com.example.demo.mypage.dto.RecentViewedPropertyRequest;
//import com.example.demo.mypage.dto.MyPropertyResponse;
//import com.example.demo.mypage.service.RecentViewedPropertyService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@WebMvcTest(controllers = RecentViewedPropertyController.class)
//class RecentViewedPropertyControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private RecentViewedPropertyService recentViewedPropertyService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private final String BASE_URL = "/mypage/histories/recent-properties";
//
//    @Test
//    @DisplayName("최근 본 매물 저장 - 성공")
//    @WithMockUser(username = "1") // userId를 1로 설정
//    void saveRecentViewedProperty_success() throws Exception {
//        RecentViewedPropertyRequest request = new RecentViewedPropertyRequest(123L);
//
//        mockMvc.perform(post(BASE_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("최근 본 매물 조회 - 성공")
//    @WithMockUser(username = "1")
//    void getRecentViewedProperties_success() throws Exception {
//        List<MyPropertyResponse> dummyList = List.of(
//                MyPropertyResponse.builder()
//                        .propertyId(1L)
//                        .aptName("아파트 1")
//                        .area1("서울시")
//                        .thumbnailImage("https://example.com/img1.jpg")
//                        .latitude(37.123)
//                        .longitude(127.123)
//                        .build()
//        );
//
//        Mockito.when(recentViewedPropertyService.getRecentViewedList(eq(1L))).thenReturn(dummyList);
//
//        mockMvc.perform(get(BASE_URL))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.recentViewedProperties").isArray())
//                .andExpect(jsonPath("$.recentViewedProperties[0].propertyId").value(1L))
//                .andExpect(jsonPath("$.recentViewedProperties[0].aptName").value("아파트 1"));
//    }
//
//}
