package com.example.demo.review.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "review_summary_category")
public class ReviewSummaryCategory {

    @Id
    @GeneratedValue
    @Column(name = "review_summary_category_id")
    private Long id;

    private Long review_summary_id;

    @Enumerated(EnumType.STRING)
    private CategoryType type;  //'TRANSPORTATION','RESIDENTIAL','EDUCATION','FACILITIES'

    private float rating;

    private String content;




}
