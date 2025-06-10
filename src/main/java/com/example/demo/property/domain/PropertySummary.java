package com.example.demo.property.domain;

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
public class PropertySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertySummaryId; //매물요약 아이디(인조)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "property_id")
    private Property property; //매물 엔티티

    private String summary; //매물 요약

    private LocalDateTime createdAt; //등록일
    private LocalDateTime updatedAt; //수정일
    private LocalDateTime deletedAt; //삭제일
}
