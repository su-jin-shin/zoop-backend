package com.example.demo.mypage.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RecentViewedGroupResponse {

    private Long realtyId;
    private String realtorName;
    private String establishRegistrationNo;
    private String address;
    private String dealCount;
    private String leaseCount;
    private String rentCount;
    private String representativeName;
    private List<RecentViewedPropertyResponse> propertie;

    public List<RecentViewedPropertyResponse> getPropertie() {
        return propertie == null ? null : new ArrayList<>(propertie);
    }

    public void setPropertie(List<RecentViewedPropertyResponse> propertie) {
        this.propertie = propertie == null ? null : new ArrayList<>(propertie);
    }
}
