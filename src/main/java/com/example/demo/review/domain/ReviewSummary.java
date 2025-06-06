package com.example.demo.review.domain;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review_summary")
@Getter
@Setter
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewSummary {

    @Id
    @GeneratedValue
    @Column(name = "review_summary_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "complex_id")
    private Complex complex;

    private float overall_rating;

    @OneToMany(mappedBy = "reviewSummary")
    private List<ReviewSummaryCategory> categories = new ArrayList<>();

    @OneToMany(mappedBy = "reviewSummary")
    private List<ReviewSummaryInsight> insights = new ArrayList<>();
}
