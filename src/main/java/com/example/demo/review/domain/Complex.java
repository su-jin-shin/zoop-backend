package com.example.demo.review.domain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "complex")
@Getter
@Setter
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class Complex {

    @Id
    @GeneratedValue
    @Column(name =  "complex_id")
    private Long id;

    private Long complex_no;  //실제 단지 번호

    private String complex_name;   //단지 이름


    @OneToMany(mappedBy = "complex")
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(mappedBy = "complex",fetch = FetchType.LAZY)
    private ReviewSummary reviewSummary;


}
