package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import com.example.demo.common.exception.UserNotFoundException;
import com.example.demo.mypage.dto.MyCommentQuery;
import com.example.demo.mypage.dto.MyCommentResponse;
import com.example.demo.mypage.repository.MyReviewCommentRepository;
import com.example.demo.property.service.PropertyService;
import com.example.demo.review.domain.ReviewComment;
import com.example.demo.review.repository.ReviewCommentLikeRepository;
import com.example.demo.review.repository.ReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyCommentService {

    private final MyReviewCommentRepository reviewCommentRepository;

    public List<MyCommentResponse> getMyComments(Long userId) {
        log.info("💬 MyCommentService 진입 userId={}", userId);

        List<MyCommentQuery> comments = reviewCommentRepository.findMyComments(userId);
        log.info("💬 댓글 수 = {}", comments.size());

        List<Long> commentIds = comments.stream()
                .map(MyCommentQuery::getCommentId)
                .toList();

        Map<Long, Boolean> isLikedMap = reviewCommentRepository.getIsLikedMapByCommentIds(commentIds, userId);

        return comments.stream()
                .map(q -> convertToDto(q, isLikedMap.getOrDefault(q.getCommentId(), false)))
                .toList();
    }

    private MyCommentResponse convertToDto(MyCommentQuery q, boolean isLiked) {
        MyCommentResponse.Item item = new MyCommentResponse.Item(
                q.getComplexId(),
                q.getPropertyId(),
                q.getArticleName()
        );

        MyCommentResponse.Review review = new MyCommentResponse.Review(
                q.getReviewId(),
                q.getReviewContent(),
                item
        );

        return new MyCommentResponse(
                q.getCommentId(),
                q.getContent(),
                q.getCreatedAt(),
                q.getLikeCount(),
                review,
                isLiked
        );
    }
}