package com.example.demo.mypage.service;

import com.example.demo.mypage.dto.MyCommentQuery;
import com.example.demo.mypage.dto.MyCommentResponse;
import com.example.demo.mypage.repository.MyReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyCommentService {

    private final MyReviewCommentRepository reviewCommentRepository;

    public List<MyCommentResponse> getMyComments(Long userId) {
        return reviewCommentRepository.findMyComments(userId).stream()
                .map(this::convertToDto)
                .toList();
    }

    private MyCommentResponse convertToDto(MyCommentQuery q) {
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
                review
        );
    }
}
