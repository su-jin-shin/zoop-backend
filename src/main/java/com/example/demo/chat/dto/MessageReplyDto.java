package com.example.demo.chat.dto;

import com.example.demo.common.excel.PropertyExcelDto;
import lombok.Data;

import java.util.List;

@Data
public class MessageReplyDto {

    private String content; // ai의 답변
    private List<PropertyExcelDto> properties;
    //private boolean isRecommended;

    public MessageReplyDto generateAiResponse(String aiReply, List<PropertyExcelDto> recommendedProperties, boolean isRecommended) {
        this.content = aiReply;
        this.properties = recommendedProperties;
        //this.isRecommended = isRecommended;
        return this;
    }

}
