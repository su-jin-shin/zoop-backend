package com.example.demo.review.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.review.dto.Review.ReviewCreateRequest;
import com.example.demo.review.dto.Review.ReviewResponse;
import com.example.demo.review.repository.ReviewRepository;
import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CreateReviewTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private UserInfo testUser;
    private Long propertyId = 101L;

    @Autowired
    private EntityManager em;

    private UserInfo testUser;
    private Long propertyId = 101L;

    @BeforeEach
    void setUp() {
        // 필드에 직접 값 세팅 (Setter 없이 생성)
        testUser = new UserInfo();
        setField(testUser, "nickname", "리뷰유저");
        setField(testUser, "email", "reviewuser@example.com");

        userInfoRepository.save(testUser);
        em.flush(); // DB 반영
    }

    @Test
    void 리뷰_생성_성공() {
        // given
        ReviewCreateRequest request = new ReviewCreateRequest();
        setField(request, "content", "테스트 리뷰입니다.");
        setField(request, "rating", 4.5);
        setField(request, "isResident", true);
        setField(request, "hasChildren", false);

        // when
        ReviewResponse response = reviewService.createReview(propertyId, request, testUser);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo("테스트 리뷰입니다.");
        assertThat(response.getRating()).isEqualTo(4.5);
        assertThat(response.getIsResident()).isTrue();
        assertThat(response.getHasChildren()).isFalse();
        assertThat(response.getUserId()).isEqualTo(testUser.getUserId());
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("필드 설정 실패: " + fieldName, e);
        }
    }


}
