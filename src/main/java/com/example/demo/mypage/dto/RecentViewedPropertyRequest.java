package com.example.demo.mypage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecentViewedPropertyRequest {
    private Long propertyId;

    public RecentViewedPropertyRequest(Long propertyId) {
        this.propertyId = propertyId;
    }

    public RecentViewedPropertyRequest() {}
}
