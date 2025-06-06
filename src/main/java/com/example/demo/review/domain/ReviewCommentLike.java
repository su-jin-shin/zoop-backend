package com.example.demo.review.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "review_comment_like")
@Getter
@Setter
public class ReviewCommentLike {

    @Id
    @GeneratedValue
    @Column(name = "review_comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private ReviewComment reviewComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Members members;

    private boolean is_liked;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    private LocalDateTime deleted_at;

}
