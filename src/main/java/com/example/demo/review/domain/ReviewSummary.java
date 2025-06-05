package com.example.demo.review.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "review_summary")
@Getter
@Setter
public class ReviewSummary {

    @Id
    @GeneratedValue
    @Column(name = "review_summary_id")
    private Long id;

    private Long complex_id;

    private float overall_rating;
}
