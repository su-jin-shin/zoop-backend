package com.example.demo.common.excel;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class PropertyExcelMetaProvider {

    public List<String> getHeaders() {
        return List.of(
                "순번", "매물명", "거래유형", "월세", "보증금", "매매가", "기타비용", "입주가능일",
                "매물특징", "상세설명", "공급면적", "전용면적", "방향", "층정보", "노출주소",
                "위도", "경도", "태그",
                "중개사명", "대표자명", "개설등록번호", "중개사주소", "대표전화번호", "휴대폰번호", "최대중개보수", "중개보수"
        );
    }

    public List<Function<PropertyExcelDto, Object>> getExtractors() {
        return List.of(
                PropertyExcelDto::getOrder,
                PropertyExcelDto::getArticleName,
                PropertyExcelDto::getTradeTypeName,
                PropertyExcelDto::getRentPrice,
                PropertyExcelDto::getWarrantPrice,
                PropertyExcelDto::getDealPrice,
                PropertyExcelDto::getEtcFeeAmount,
                PropertyExcelDto::getMoveInPossibleYmd,

                PropertyExcelDto::getArticleFeatureDesc,
                PropertyExcelDto::getDetailDescription,
                PropertyExcelDto::getArea1,
                PropertyExcelDto::getArea2,
                PropertyExcelDto::getDirection,
                PropertyExcelDto::getFloorInfo,
                PropertyExcelDto::getExposureAddress,

                PropertyExcelDto::getLatitude,
                PropertyExcelDto::getLongitude,
                dto -> String.join(", ", dto.getTagList() == null ? List.of() : dto.getTagList()),

                PropertyExcelDto::getRealtorName,
                PropertyExcelDto::getRepresentativeName,
                PropertyExcelDto::getEstablishRegistrationNo,
                PropertyExcelDto::getRealtorAddress,
                PropertyExcelDto::getRepresentativeTelNo,
                PropertyExcelDto::getCellPhoneNo,
                PropertyExcelDto::getMaxBrokerFee,
                PropertyExcelDto::getBrokerFee
        );
    }
}
