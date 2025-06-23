package com.example.demo.chat.dto;

import com.example.demo.chat.type.SenderType;
import com.example.demo.common.excel.PropertyExcelDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MessageDto {

    private Long chatRoomId;
    private Long messageId;
    private String content;
    private SenderType senderType;

    @Getter(AccessLevel.NONE)
    private List<PropertyExcelDto> properties;

    private LocalDateTime createdAt;

    public List<PropertyExcelDto> getProperties() {
        return properties == null ? null : new ArrayList<>(properties); // 복사본 반환
    }

    public void setProperties(List<PropertyExcelDto> properties) {
        this.properties = properties == null ? null : new ArrayList<>(properties); // 복사본 저장
    }

    public static class MessageDtoBuilder {
        public MessageDtoBuilder properties(List<PropertyExcelDto> properties) {
            this.properties = properties == null ? null : new ArrayList<>(properties);
            return this;
        }
    }

}
