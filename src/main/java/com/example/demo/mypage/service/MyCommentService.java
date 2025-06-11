package com.example.demo.mypage.service;

import com.example.demo.mypage.dto.MyCommentQueryDto;
import com.example.demo.mypage.dto.MyCommentResponse;
import com.example.demo.mypage.repository.ReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyCommentService {

    private final ReviewCommentRepository reviewCommentRepository;

    public List<MyCommentResponse> getMyComments(Long userId) {
        return reviewCommentRepository.findMyComments(userId).stream()
                .map(this::convertToDto)
                .toList();
    }

    private MyCommentResponse convertToDto(MyCommentQueryDto q) {
        MyCommentResponse dto = new MyCommentResponse();
        dto.setCommentId(q.getCommentId());
        dto.setContent(q.getContent());
        dto.setCreatedAt(q.getCreatedAt());
        dto.setLikeCount(q.getLikeCount());

        MyCommentResponse.Review review = new MyCommentResponse.Review();
        review.setReviewId(q.getReviewId());
        review.setContent(q.getReviewContent());

        MyCommentResponse.Item item = new MyCommentResponse.Item();
        item.setComplexId(q.getComplexId());
        item.setPropertyId(q.getPropertyId());
        item.setArticleName(q.getArticleName());

        review.setItem(item);
        dto.setReview(review);

        return dto;
    }
}
