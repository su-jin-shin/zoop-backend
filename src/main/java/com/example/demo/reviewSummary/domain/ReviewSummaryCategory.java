package com.example.demo.reviewSummary.domain;

import com.example.demo.review.domain.*;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "review_summary_category")
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2","UUF_UNUSED_FIELD"})
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

    @Column(name = "rating",precision = 2, scale = 1, nullable = false)
    private BigDecimal rating;

    private String content;




}
