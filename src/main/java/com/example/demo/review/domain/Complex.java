package com.example.demo.review.domain;


import com.example.demo.reviewSummary.domain.ReviewSummary;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "complex"
,uniqueConstraints = @UniqueConstraint(columnNames = {"complex_no"}))
@Getter
@Setter
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class Complex {

    @Id
    @GeneratedValue
    @Column(name =  "complex_id")
    private Long id;

    @Column(name = "complex_no",nullable = false)
    private String complexNo;  //실제 단지 번호 --> 매물(property_id)와 매핑용

    @Column(name = "complex_name")
    private String complexName;   //단지 이름


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

//    @OneToOne(mappedBy = "complex",fetch = FetchType.LAZY)
//    private ReviewSummary reviewSummary;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }



}
