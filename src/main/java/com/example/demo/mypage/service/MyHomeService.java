package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.mypage.dto.*;
import com.example.demo.realty.dto.PropertyListItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyHomeService {

    private final UserInfoRepository userInfoRepository;
    private final MyReviewService myReviewService;
    private final MyCommentService myCommentService;
    private final BookmarkedPropertyService bookmarkedPropertyService;
    private final RecentViewedPropertyService recentViewedPropertyService;

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new UserNotFoundException();
        }
    }

    @Value("${app.base-url}")
    private String baseUrl;

    public MyPageHomeResponse getHomeInfo(Long userId) {
        validateUserId(userId);

        // 1. 사용자 닉네임 및 프로필 이미지 조회
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        String nickname = user.getNickname();
        String profileImageUrl = user.getProfileImage();

        if (profileImageUrl == null || profileImageUrl.isBlank()) {
            profileImageUrl = "/images/default-profile.png";
        }
        if (!profileImageUrl.startsWith("http")) {
            profileImageUrl = baseUrl + profileImageUrl;
        }

        // 3. 찜한 매물 20개
        List<PropertyListItemDto> allBookmarked = bookmarkedPropertyService.getAllBookmarkedPropertyResponses(userId);
        List<PropertyListItemDto> bookmarked = allBookmarked.stream().limit(20).toList();
        int bookmarkedCount = allBookmarked.size();

        // 4. 최근 본 매물 20개
        List<PropertyListItemDto> allRecentViewed = recentViewedPropertyService.getRecentViewedList(userId);
        List<PropertyListItemDto> recentViewed = allRecentViewed.stream().limit(20).toList();
        int recentViewedCount = Math.min(allRecentViewed.size(), 20);

        // 2. 리뷰 2개 또는 코멘트 2개
        List<MyReviewResponse> reviews = myReviewService.getMyReviews(userId);

        List<?> reviewOrComments;
        if (!reviews.isEmpty()) {
            reviewOrComments = reviews.stream().limit(2).toList();
        } else {
            List<MyCommentResponse> comments = myCommentService.getMyComments(userId);
            reviewOrComments = comments.stream().limit(2).toList();
        }

        return MyPageHomeResponse.builder()
                .userInfo(new MyPageUserDto(nickname, profileImageUrl))
                .reviewOrComments(reviewOrComments)
                .activity(MyPageActivityDto.builder()
                        .bookmarkedPropertyCount(bookmarkedCount)
                        .recentViewedPropertyCount(recentViewedCount)
                        .build())
                .bookmarkedProperties(bookmarked)
                .recentViewedProperties(recentViewed)
                .build();
    }

}
