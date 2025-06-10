package com.example.demo.property.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//부동산 엔티티

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "realty")
public class Realty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long realtyId; // 부동산 아이디 (인조)

    private String establishRegistrationNo; //개설등록번호
    private String realtorName; //중개사이름
    private String representativeName; //대표자이름
    private String address; //중개사주소
    private String representativeTelNo; //대표전화번호
    private String cellPhoneNo; //휴대전화번호
    private Integer dealCount; //매매건수
    private Integer leaseCount; //전세건수
    private Integer rentCount; //월세건수
    private BigDecimal maxBrokerFee; //최대 중개보수
    private BigDecimal brokerFee; //중개보수


    private LocalDateTime createdAt; //등록일
    private LocalDateTime updatedAt; //수정일
    private LocalDateTime deletedAt; //삭제일
}
