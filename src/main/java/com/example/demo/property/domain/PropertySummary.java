package com.example.demo.property.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PropertySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertySummaryId; //매물요약 아이디(인조)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "property_id")
    private Property property; //매물 엔티티

    private String summary; //매물 요약

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;   // 지역 추가 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;   // 지역 수정 시각
    private LocalDateTime deletedAt;   // 지역 삭제 시각
}
