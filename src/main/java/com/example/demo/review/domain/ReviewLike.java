package com.example.demo.review.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_like")
@Getter
@Setter
public class ReviewLike {

    @Id
    @GeneratedValue
    @Column(name = "review_like_id")
    private Long id;

    private Long review_id;

    private Long user_id;

    private boolean is_liked;

    private LocalDateTime created_at;

    private LocalDateTime deleted_at;

}
