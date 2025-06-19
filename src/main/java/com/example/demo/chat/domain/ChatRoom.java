package com.example.demo.chat.domain;

import com.example.demo.auth.domain.UserInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room")
@Getter
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long chatRoomId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo userInfo;

    @Column(columnDefinition = "TEXT")
    @Setter
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

    // 생성자
    public ChatRoom(UserInfo userInfo) {
        this.userInfo = userInfo;
        this.title = "new chat";
        this.createdAt = LocalDateTime.now();
        this.lastMessageAt = LocalDateTime.now();
    }

    // 기본 생성자
    protected ChatRoom() {}


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
