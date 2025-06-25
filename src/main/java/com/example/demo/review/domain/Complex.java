package com.example.demo.review.domain;



import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Getter;


import java.time.LocalDateTime;

// 설계 실패로 실제로는 complex 도메인을 서비스에서 잘 사용 안함

@Entity
@Table(name = "complex"
,uniqueConstraints = @UniqueConstraint(columnNames = {"complex_no"}))
@Getter
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class Complex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "complex_id")
    private Long id;

    @Column(name = "complex_no",nullable = false)
    private String complexNo;  //실제 단지 번호

    @Column(name = "complex_name")
    private String complexName;   //단지 이름


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    protected Complex() {}  // JPA 기본 생성자

    public Complex(String complexNo, String complexName) {
        this.complexNo = complexNo;
        this.complexName = complexName;
    }



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
