package com.example.demo.reviewSummary.domain;


import com.example.demo.review.domain.Complex;
import com.example.demo.reviewSummary.domain.ReviewSummaryCategory;
import com.example.demo.reviewSummary.domain.ReviewSummaryInsight;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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

    @Column(name = "overall_rating",precision = 2, scale = 1, nullable = false)
    private BigDecimal overall_rating;




    @OneToMany(mappedBy = "reviewSummary")
    private List<ReviewSummaryCategory> categories = new ArrayList<>();

    @OneToMany(mappedBy = "reviewSummary")
    private List<ReviewSummaryInsight> insights = new ArrayList<>();
}


/* 추후 api 응답값이 float/double인 경우
// 외부 응답값이 double인 경우
double externalRatingFromApi = 4.5;
review.setExternalRating(BigDecimal.valueOf(externalRatingFromApi));
 */