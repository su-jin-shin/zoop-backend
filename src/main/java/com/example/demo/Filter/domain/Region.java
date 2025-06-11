package com.example.demo.Filter.domain;

import com.example.demo.Filter.domain.enums.CortarType;
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
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionId;     // 지역아이디

    @Column(unique = true, nullable = false)
    private String cortarNo;   // 지역코드

    private String cortarName;  // 지역명

    @Enumerated(EnumType.STRING)
    private CortarType cortarType;   // 지역타입

    private String parentCortarNo;   // 상위 행정 구역코드

    private Double centerLat;        // 위도

    private Double cortarLon;        // 경도

    private String fullCortarName;   // 풀지역명

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;   // 지역 추가 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;   // 지역 수정 시각
    private LocalDateTime deletedAt;   // 지역 삭제 시각

}
