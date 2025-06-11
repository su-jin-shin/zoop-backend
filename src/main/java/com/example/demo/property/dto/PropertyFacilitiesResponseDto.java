package com.example.demo.property.dto;

import com.example.demo.property.domain.Property;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" }

)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyFacilitiesResponseDto {

    private Long propertyId; //매물 아이디 (기본키)
    private String heatMethodTypeName; //난방방식
    private List<String> lifeFacilities; //생활시설
    private List<String> securityFacilities; //보안시설
    private List<String> etcFacilities; //기타시설


    //빌더
    public static  PropertyFacilitiesResponseDto of(Property p){
        return  PropertyFacilitiesResponseDto.builder()
                .propertyId(p.getPropertyId())
                .heatMethodTypeName(p.getHeatMethodTypeName())
                .lifeFacilities(safeList(p.getLifeFacilities()))
                .securityFacilities(safeList(p.getSecurityFacilities()))
                .etcFacilities(safeList(p.getEtcFacilities()))
                .build();
    }


    //null 방지 메서드
    private static List<String> safeList(List<String> list){
        return  list == null ? Collections.emptyList() : list;
    }



}
