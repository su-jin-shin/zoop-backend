package com.example.demo.property.domain;

import com.example.demo.property.domain.enums.ImageType;
import com.example.demo.review.domain.Complex;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" },
        justification = "Complex는 JPA 연관 엔티티이며, LAZY 로딩 및 setter 없이 관리되므로 외부 변경 위험이 없습니다."
)
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


