package com.example.demo.mypage.dto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "방어적 복사 완료. 외부에 mutable list 노출 안 됨."
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentViewedPropertyResponse {

    private Integer order; // 정렬 순서
    private Long propertyId;
    private String tradeTypeName;
    private BigDecimal rentPrice;
    private BigDecimal warrantPrice;
    private BigDecimal dealPrice;
    private String dealOrWarrantPrc;

    private List<String> summary;

    private String articleName;
    private String aptName;
    private String buildingName;
    private String buildingTypeName;
    private String realestateTypeName;
    private String area1;
    private Boolean isActive;
    private Boolean isBookmarked;
    private String thumbnailImage;

    public List<String> getSummary() {
        return summary == null ? null : new ArrayList<>(summary);
    }

    public void setSummary(List<String> summary) {
        this.summary = summary == null ? null : new ArrayList<>(summary);
    }

    public static class RecentViewedPropertyResponseBuilder {
        public RecentViewedPropertyResponseBuilder summary(List<String> summary) {
            this.summary = summary == null ? null : new ArrayList<>(summary);
            return this;
        }
    }
}
