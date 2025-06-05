package com.example.demo.review.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;

@Entity
@Table(name = "review_summary_category")
@SuppressFBWarnings(value = "UWF_UNWRITTEN_FIELD",
        justification = "unused SpotBugs 무시/")
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
