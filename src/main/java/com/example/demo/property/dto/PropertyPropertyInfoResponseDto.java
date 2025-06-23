package com.example.demo.property.dto;

import com.example.demo.property.domain.Image;
import com.example.demo.property.domain.Property;
import com.example.demo.property.domain.enums.ImageType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.property.util.PropertyDtoConverter.convertImages;

@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2"}

)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyPropertyInfoResponseDto {

    private Long propertyId; //매물 아이디 (기본키)

    private String area2; //전용면적
    private String area1; //공급 면적
    private String principalUse; //주용도1
    private String realEstateTypeName; //부동산유형명(주용도 1에대한 빌라 사용)
    private String aptName; //아파트이름 (확인 필요)
    private String buildingName; //건물이름
    private String floorInfo; //층정보
    private String roomCount; //방개수
    private String bathroomCount; //욕실개수
    private String directionBaseTypeName; //기준방향명
    private String direction; //방향
    private String entranceTypeName; //현관형태명
    private String householdCount; //세대수
    private String parkingCount; //총주차대수
    private String parkingCountPerHousehold; //세대당주차대수
    private String parkingPossibleYN; //주차가능여부
    private String useApproveYmd; //사용승인일
    private List<ImageDto> images; //이미지 리스트





    //빌더
    public static PropertyPropertyInfoResponseDto of(Property property , List<Image> images) {
        List<Image> structureImages = images == null ? Collections.emptyList() :
                images.stream()
                        .filter(image -> ImageType.STRUCTURE.equals(image.getImageType()))
                        .collect(Collectors.toList());

        String parkingCountStr = property.getParkingCount();
        String parkingPossibleYN = "N";

        if (parkingCountStr != null) {
            try {
                int count = Integer.parseInt(parkingCountStr);
                if (count > 0) {
                    parkingPossibleYN = "Y";
                }
            } catch (NumberFormatException ignored) {
                parkingPossibleYN = "N"; // 예외 시 기본값
            }
        }

        return PropertyPropertyInfoResponseDto.builder()
                .propertyId(property.getPropertyId())
                .area2(property.getArea2())
                .area1(property.getArea1())
                .principalUse(property.getPrincipalUse())
                .realEstateTypeName(property.getRealEstateTypeName())
                .aptName(property.getAptName())
                .buildingName(property.getBuildingName())
                .floorInfo(property.getFloorInfo())
                .roomCount(property.getRoomCount())
                .bathroomCount(property.getBathroomCount())
                .directionBaseTypeName(property.getDirectionBaseTypeName())
                .direction(property.getDirection())
                .entranceTypeName(property.getEntranceTypeName())
                .householdCount(property.getHouseholdCount())
                .parkingCount(property.getParkingCount())
                .parkingCountPerHousehold(property.getParkingCountPerHousehold())
                .parkingPossibleYN(parkingPossibleYN)
                .useApproveYmd(property.getUseApproveYmd())
                .images(List.copyOf(convertImages(structureImages)))
                .build();
    }


}
