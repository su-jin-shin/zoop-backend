package com.example.demo.review.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="review_comment")
@Getter
@Setter
public class ReviewComment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private Long review_id;

    private Long user_id;

    private String content;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;

}
