package com.example.demo.Filter.domain;

import com.example.demo.Filter.domain.enums.RealEstateTypeName;
import com.example.demo.Filter.domain.enums.TradeTypeName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filterId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    private String filterTitle;       // 필터 제목

    @Enumerated(EnumType.STRING)
    private TradeTypeName tradeTypeName;   // 거래 타입

    @Enumerated(EnumType.STRING)
    private RealEstateTypeName realEstateTypeName;     // 매물타입

    private String dealOrWarrantPrc;                   // 보증금/전세금/매매가

    private BigDecimal rentPrice;                      //월세

    private LocalDateTime createdAt;                   // 필터 생성 시각


}
