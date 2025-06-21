package com.example.demo.Filter.domain;

import com.example.demo.Filter.domain.enums.TradeTypeCode;
import com.example.demo.Filter.domain.enums.TradeTypeName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filterId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    private String filterTitle;       // 필터 제목

    private String longitude;                 // 경도

    private String latitude;                // 위도

    private String bCode;           // 법정 코드

    private String hCode;           // 행정 코드

    @Enumerated(EnumType.STRING)
    private TradeTypeName tradeTypeName;   // 거래 타입  // 매매, 전세, 월세

    @Enumerated(EnumType.STRING)
    private TradeTypeCode tradeTypeCode;    // A1, B1, B2

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "filter_real_estate_types", // 서브 테이블 이름
            joinColumns = @JoinColumn(name = "filter_id") // 외래키 이름
    )
    @Column(name = "real_estate_type") // 저장될 실제 값의 컬럼 이름
    private List<String> realEstateTypeName;     // 매물타입 // 아파트, 오피스텔, 원룸_투룸

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "filter_real_estate_type_codes",
            joinColumns = @JoinColumn(name = "filter_id")
    )
    @Column(name = "real_estate_type_code")
    private List<String> realEstateTypeCode;    // APT, OPST, VL:YR, DDDGG:DSD

    private String dealOrWarrantPrc;                   // 보증금/전세금/매매가

    private BigDecimal rentPrice;                      //월세

    private LocalDateTime createdAt;                   // 필터 생성 시각


    public Long getFilterId() {
        return filterId;
    }

    public Region getRegion() {
        return region;
    }

    public String getFilterTitle() {
        return filterTitle;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getBCode() {
        return bCode;
    }

    public String getHCode() {
        return hCode;
    }

    public TradeTypeName getTradeTypeName() {
        return tradeTypeName;
    }

    public TradeTypeCode getTradeTypeCode() {
        return tradeTypeCode;
    }

    public List<String> getRealEstateTypeName() {
        return realEstateTypeName == null ? null : new ArrayList<>(realEstateTypeName);
    }

    public List<String> getRealEstateTypeCode() {
        return realEstateTypeCode == null ? null : new ArrayList<>(realEstateTypeCode);
    }

    public String getDealOrWarrantPrc() {
        return dealOrWarrantPrc;
    }

    public BigDecimal getRentPrice() {
        return rentPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
