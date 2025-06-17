package com.example.demo.chat.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room")
@Getter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long chatRoomId;

    @Setter
    @Column
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column
    private LocalDateTime titleUpdatedAt;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime lastMessageAt;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column
    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        if (title == null) title = "new chat";
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (lastMessageAt == null) lastMessageAt = LocalDateTime.now();
    }

    public void updateTitle(String newTitle) {
        this.title = newTitle;
        this.titleUpdatedAt = LocalDateTime.now();
    }

    public void updateLastMessageAt(LocalDateTime time) {
        this.lastMessageAt = time;
    }

}
