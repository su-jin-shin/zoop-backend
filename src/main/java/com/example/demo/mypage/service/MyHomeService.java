package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.mypage.domain.BookmarkedProperty;
import com.example.demo.mypage.domain.RecentViewedProperty;
import com.example.demo.mypage.dto.MyCommentResponse;
import com.example.demo.mypage.dto.MyPageHomeResponse;
import com.example.demo.mypage.dto.MyPageUserDto;
import com.example.demo.mypage.dto.MyReviewResponse;
import com.example.demo.mypage.repository.BookmarkedPropertyRepository;
import com.example.demo.mypage.repository.MyReviewRepository;
import com.example.demo.property.domain.Property;
import com.example.demo.realty.dto.PropertyListItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyHomeService {

    private final UserInfoRepository userInfoRepository;
    private final MyReviewService myReviewService;
    private final MyCommentService myCommentService;
    private final BookmarkedPropertyService bookmarkedPropertyService;
    private final RecentViewedPropertyService recentViewedPropertyService;

    public MyPageHomeResponse getHomeInfo(Long userId) {
        log.info("📌 [getHomeInfo] userId = {}", userId);

        // 1. 사용자 닉네임 및 프로필 이미지 조회
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        String nickname = user.getNickname();
        String profileImage = user.getProfileImage();
        log.info("✅ 사용자 이름 = {}", nickname);

        // 3. 찜한 매물 20개
        log.info("🔍 찜한 매물 조회 시도");
        List<PropertyListItemDto> allBookmarked = bookmarkedPropertyService.getAllBookmarkedPropertyResponses(userId);
        List<PropertyListItemDto> bookmarked = allBookmarked.stream().limit(20).toList();
        int bookmarkedCount = allBookmarked.size();
        log.info("✅ 찜한 매물 수 = {}", bookmarkedCount);

        // 4. 최근 본 매물 20개
        log.info("🔍 최근 본 매물 조회 시도");
        List<PropertyListItemDto> allRecentViewed = recentViewedPropertyService.getRecentViewedList(userId);
        List<PropertyListItemDto> recentViewed = allRecentViewed.stream().limit(20).toList();
        int recentViewedCount = Math.min(allRecentViewed.size(), 20);
        log.info("✅ 최근 본 매물 수 = {}", recentViewedCount);


        // 2. 리뷰 2개 또는 코멘트 2개
        log.info("🔍 리뷰 조회 시도");
        List<MyReviewResponse> reviews = myReviewService.getMyReviews(userId);
        log.info("✅ 리뷰 수 = {}", reviews.size());

        List<?> reviewOrComments;
        if (!reviews.isEmpty()) {
            reviewOrComments = reviews.stream().limit(2).toList();
        } else {
            log.info("🔍 리뷰 조회 시도");
            List<MyCommentResponse> comments = myCommentService.getMyComments(userId);
            log.info("✅ 코멘트 수 = {}", comments.size());
            reviewOrComments = comments.stream().limit(2).toList();
        }

        return MyPageHomeResponse.builder()
                .userInfo(new MyPageUserDto(nickname, profileImage))
                .reviewOrComments(reviewOrComments)
                .bookmarkedProperties(bookmarked)
                .bookmarkedCount(bookmarkedCount)
                .recentViewedProperties(recentViewed)
                .recentViewedCount(recentViewedCount)
                .build();
    }

}
