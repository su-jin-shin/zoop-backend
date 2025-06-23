package com.example.demo.chat.dto;

import com.example.demo.chat.type.SenderType;
import com.example.demo.common.excel.PropertyExcelDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.util.List;

@Data
@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "Defensive copies are used appropriately"
)
public class MessageReplyDto {

    private String content; // ai의 답변
    private List<PropertyExcelDto> properties;
    private SenderType senderType;
    //private boolean isRecommended;

    public MessageReplyDto generateAiResponse(String aiReply, List<PropertyExcelDto> recommendedProperties, boolean isRecommended) {
        this.content = aiReply;
        this.properties = recommendedProperties;
        this.senderType = SenderType.CHATBOT;
        //this.isRecommended = isRecommended;
        return this;
    }

}
