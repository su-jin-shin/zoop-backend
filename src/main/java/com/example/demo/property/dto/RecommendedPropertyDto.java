package com.example.demo.property.dto;

import com.example.demo.common.excel.PropertyExcelDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class RecommendedPropertyDto {

    private String articleNo;
    private PropertyExcelDto propertyExcelDto;
    private Map<String, List<String>> aiSummary;
    private Long propertyId;

    public void applyPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public RecommendedPropertyDto(String articleNo, PropertyExcelDto propertyExcelDto, Map<String, List<String>> aiSummary) {
        this.articleNo = articleNo;
        this.propertyExcelDto = propertyExcelDto;
        this.aiSummary = aiSummary;
    }

}
