package com.example.demo.property.domain;

import com.example.demo.property.domain.enums.ImageType;
import com.example.demo.review.domain.Complex;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId; //이미지 아이디 (인조)



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property; //매물 (외래키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complex_id")
    private Complex complex; //단지(외래키)

    @Column(columnDefinition = "TEXT")
    private String imageUrl; // 이미지 경로

    @Enumerated(EnumType.STRING)
    private ImageType imageType; //이미지 타입

    private Integer imageOrder; //이미지 순서
    private Boolean isMain; //대표이미지 여부

    private LocalDateTime createdAt; //등록일
    private LocalDateTime updatedAt; //수정일
    private LocalDateTime deletedAt; //삭제일
}


