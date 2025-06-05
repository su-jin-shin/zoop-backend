package com.example.demo.review.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "review_summary_insight")
@Getter
@Setter
public class ReviewSummaryInsight {

    @Id
    @GeneratedValue
    @Column(name = "review_summary_insight_id")
    private Long id;

    private Long review_summary_id;


    private InsightType type;  //'GOOD' , 'BAD'

    private String content;
}
