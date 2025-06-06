package com.example.demo.review.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;

@Entity
@Table(name = "review_summary_category")
@SuppressFBWarnings(value = "UUF_UNUSED_FIELD",
        justification = "content, rating, review_summary_id 필드는 ORM 매핑용으로 사용되므로 SpotBugs 무시")
public class ReviewSummaryCategory {

    @Id
    @GeneratedValue
    @Column(name = "review_summary_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_summary_id")
    private ReviewSummary reviewSummary;

    @Enumerated(EnumType.STRING)
    private CategoryType type;  //'TRANSPORTATION','RESIDENTIAL','EDUCATION','FACILITIES'

    private float rating;

    private String content;




}
