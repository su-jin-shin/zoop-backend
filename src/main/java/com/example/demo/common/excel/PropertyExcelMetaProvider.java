package com.example.demo.common.excel;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class PropertyExcelMetaProvider {

    public List<String> getHeaders() {
        return List.of(
                "거래 유형", "월세", "보증금", "매매가", "매매 또는 보증금 가격", "요약 키워드",
                "아파트 이름", "건물 이름", "부동산 유형", "전용 면적", "매물 이름", "방향", "층 정보", "노출 주소",
                "관리비(기타 비용)", "입주 가능일", "매물 특징 요약", "상세 설명",
                "중개사명", "대표자명", "중개사 주소", "대표 전화번호", "휴대폰 번호", "최대 중개보수", "중개보수"
        );
    }

    public List<Function<PropertyExcelDto, Object>> getExtractors() {

        return List.of(
                PropertyExcelDto::getTradeTypeName,
                PropertyExcelDto::getRentPrice,
                PropertyExcelDto::getWarrantPrice,
                PropertyExcelDto::getDealOrWarrantPrc,
                dto -> String.join(", ", dto.getTagList() == null ? List.of() : dto.getTagList()),
                PropertyExcelDto::getAptName,
                PropertyExcelDto::getBuildingName,
                PropertyExcelDto::getRealEstateTypeName,
                PropertyExcelDto::getArea2,

                PropertyExcelDto::getArticleName,

                PropertyExcelDto::getDirection,
                PropertyExcelDto::getFloorInfo,
                PropertyExcelDto::getExposureAddress,

                PropertyExcelDto::getEtcFeeAmount,
                PropertyExcelDto::getMoveInPossibleYmd,
                PropertyExcelDto::getArticleFeatureDesc,
                PropertyExcelDto::getDetailDescription,

                PropertyExcelDto::getRealtorName,
                PropertyExcelDto::getRepresentativeName,
                PropertyExcelDto::getRealtorAddress,
                PropertyExcelDto::getRepresentativeTelNo,
                PropertyExcelDto::getCellPhoneNo,
                PropertyExcelDto::getMaxBrokerFee,
                PropertyExcelDto::getBrokerFee
        );
    }
}
