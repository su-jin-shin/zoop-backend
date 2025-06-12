package com.example.demo.property.domain;

import com.example.demo.property.domain.enums.ImageType;
import com.example.demo.review.domain.Complex;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" },
        justification = "Complex는 JPA 연관 엔티티이며, LAZY 로딩 및 setter 없이 관리되므로 외부 변경 위험이 없습니다."
)
@Entity
@Table(name = "image",
        uniqueConstraints = {
                @UniqueConstraint(name = "property_unique_image", columnNames = {"property_id", "image_type", "image_order"}),
                @UniqueConstraint(name = "complex_unique_image", columnNames = {"complex_id", "image_type", "image_order"})
        }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;   // 지역 추가 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;   // 지역 수정 시각
    private LocalDateTime deletedAt;   // 지역 삭제 시각


}