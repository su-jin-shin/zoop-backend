package com.example.demo.property.domain;


import com.example.demo.property.converter.JsonStringListConverter;
import com.example.demo.realty.domain.Realty;
import com.example.demo.review.domain.Complex;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" },
        justification = "Property 클래스의 복합 필드는 JPA 관리 하에 있으며, 외부에서 수정되지 않도록 안전하게 사용됩니다."
)

@Entity
@Table(name = "property", uniqueConstraints = {
        @UniqueConstraint(name = "unique_article_no", columnNames = "article_no")
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId; //기본키 인조 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "realty_id")
    private Realty realty; //부동산 (외래키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complex_id")
    private Complex complex; //단지 (외래키)

    @Column(name = "article_no", nullable = false)
    private String articleNo; //매물번호 (unique)
    private String articleName; //매물이름
    private String aptName; //아파트이름
    private String buildingName; //건물이름

    @Column(name = "trade_complete_yn")
    private String tradeCompleteYN; //매물 확인일
    private String heatMethodTypeName; //난방방식
    private String heatFuelTypeName; //난방연료
    private String householdCount; //세대수

    @Column(name = "use_approve_ymd")
    private String useApproveYmd; //사용승인일
    private String realEstateTypeName; //부동산유형명
    private String tradeTypeName; //거래유형명
    private String cityName; //도시이름
    private String divisionName; //구군명
    private String sectionName; //동이름
    private String walkingTimeToNearSubway; //지하철 도보시간
    private String roomCount; //방개수
    private String bathroomCount; //욕실개수
    private String moveInTypeName; //입주형태명

    private String moveInPossibleYmd; //입주가능일

    @Column(columnDefinition = "TEXT")
    private String articleFeatureDescription; //매물특징설명

    @Column(columnDefinition = "TEXT")
    private String detailDescription; //상세설명

    @Column(name = "parking_possible_yn")
    private String parkingPossibleYN; //주차가능여부
    private String principalUse; //주용도1
    private String mainPurpsCdNm; //주용도2
    private String floorInfo; //층정보
    private String dealOrWarrantPrc; //거래 또는 보증금 가격


    private String area1; //공급면적
    private String area2; //전용면적
    private String direction; //방향  

    @Column(columnDefinition = "TEXT")
    private String articleFeatureDesc; //매물특징요약
    private String sameAddrMaxPrc; //동일주소 최고가
    private String sameAddrMinPrc; //동일주소 최저가
    private String directionBaseTypeName; //기준방향명
    private String entranceTypeName; //현관형태명


    @Column(name = "life_facilities", columnDefinition = "jsonb")
    @Convert(converter = JsonStringListConverter.class)
    private List<String> lifeFacilities; //생활시설


    @Column(name ="security_facilities", columnDefinition = "jsonb")
    @Convert(converter = JsonStringListConverter.class)
    private List<String> securityFacilities; //보안시설

    @Column(name = "etc_facilities", columnDefinition = "jsonb")
    @Convert(converter = JsonStringListConverter.class)
    private List<String> etcFacilities; //기타시설

    private String totalFloorCount; //총층수
    private String correspondingFloorCount; //해당층수
    private String parkingCount; //총주차대수

    @Column(name = "parking_count_per_household")
    private String parkingCountPerHousehold; //세대당주차대수
    private Double latitude; //위도
    private Double longitude; //경도
    private String exposureAddress; //노출주소

    @Column(name = "expose_start_ymd")
    private String exposeStartYMD; //노출시작일
    private BigDecimal rentPrice; //월세
    private BigDecimal dealPrice; //매매가
    private BigDecimal warrantPrice; //보증금
    private BigDecimal allWarrantPrice; //총보증금

    private BigDecimal allRentPrice; //총월세
    private BigDecimal priceBySpace; //면적당가격
    private BigDecimal acquisitionTax; //취득세
    private BigDecimal registTax; //등록세
    private BigDecimal specialTax; //지방교육세

    private BigDecimal eduTax; //교육세
    private BigDecimal financePrice; //융자가격
    private BigDecimal etcFeeAmount; //관리비(기타비용)


    @Column(name = "tag_list", columnDefinition = "jsonb")
    @Convert(converter = JsonStringListConverter.class)
    private List<String> tagList; //태그

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;   // 지역 추가 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;   // 지역 수정 시각
    private LocalDateTime deletedAt;   // 지역 삭제 시각


}
