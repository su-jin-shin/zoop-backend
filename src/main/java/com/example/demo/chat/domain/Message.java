package com.example.demo.chat.domain;

import com.example.demo.chat.type.SenderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@NoArgsConstructor
public class Message {

    public Message(ChatRoom chatRoom, SenderType senderType, String content) {
        this.chatRoom = chatRoom;
        this.senderType = senderType;
        this.content = content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Getter
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @JdbcType(PostgreSQLEnumJdbcType.class)
    private SenderType senderType;

    @Column(columnDefinition = "TEXT")
    @Getter
    private String content;

    @Column
    @Getter
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

}
