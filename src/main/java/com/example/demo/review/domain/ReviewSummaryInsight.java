package com.example.demo.review.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "review_summary_insight")
@Getter
@Setter
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ReviewSummaryInsight {

    @Id
    @GeneratedValue
    @Column(name = "review_summary_insight_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_summary_id")
    private ReviewSummary reviewSummary;

    @Enumerated(EnumType.STRING)
    private InsightType type;  //'GOOD' , 'BAD'

    private String content;
}
