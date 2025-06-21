package com.example.demo.Filter.domain;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.chat.domain.ChatRoom;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuppressFBWarnings(
        value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" }
)
public class ChatFilterHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatFilterHistoryId;                       // 채팅필터히스토리 아이디

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;                              // 채팅룸과 연관관계

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "filter_id")
    private Filter filter;                                 // 필터와 연관관계

    private LocalDateTime usedAt;                          // 필터 사용시각


    public ChatFilterHistory(Filter filter, ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        this.filter = filter;
        this.usedAt = LocalDateTime.now();
    }
}
