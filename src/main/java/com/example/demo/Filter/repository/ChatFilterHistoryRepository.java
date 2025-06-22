package com.example.demo.Filter.repository;

import com.example.demo.Filter.domain.ChatFilterHistory;
import com.example.demo.Filter.domain.Filter;
import com.example.demo.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatFilterHistoryRepository extends JpaRepository<ChatFilterHistory, Long> {

    // 채팅 필터 히스토리에 이미 사용 중인지 확인
    //boolean existsByChatRoomAndFilter(ChatRoom chatRoom, Filter filter);
}
