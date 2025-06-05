package com.example.demo.review.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    private Long comment_id;

    private Long user_id;

    private boolean is_liked;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    private LocalDateTime deleted_at;

}
