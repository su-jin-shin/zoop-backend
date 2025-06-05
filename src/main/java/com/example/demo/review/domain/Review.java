package com.example.demo.review.domain;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name="reviews")
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    private Long complex_id;

    private Long user_id;

    private String content;

    private Long like_count;

    private boolean has_children;

    private boolean is_resident;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;
}
