package com.example.demo.chat.domain;

import com.example.demo.chat.type.SenderType;
import com.example.demo.common.excel.PropertyExcelDto;
import com.example.demo.property.domain.RecommendedProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "message")
@NoArgsConstructor
@SuppressFBWarnings(value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" }, justification = "chatRoom은 도메인 내부 객체로 신뢰 가능")
public class Message {

    public Message(ChatRoom chatRoom, SenderType senderType, String content) {
        this.chatRoom = chatRoom;
        this.senderType = senderType;
        this.content = content;
    }

    public Message(ChatRoom chatRoom, SenderType senderType, String content, List<PropertyExcelDto> properties, boolean isRecommended) {
        this.chatRoom = chatRoom;
        this.senderType = senderType;
        this.content = content;
        this.properties = properties;
        this.isRecommended = isRecommended;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Getter
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Getter
    private SenderType senderType;

    @Column(columnDefinition = "TEXT")
    @Getter
    private String content;

    @Column
    @Getter
    private boolean isRecommended;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Getter
    private List<PropertyExcelDto> properties;

    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
    private List<RecommendedProperty> recommendedProperties = new ArrayList<>();

    @Column
    @Getter
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public void updateProperties(List<PropertyExcelDto> properties) {
        this.properties = properties;
    }

}
