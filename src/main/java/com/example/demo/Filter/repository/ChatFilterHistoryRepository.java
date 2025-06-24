package com.example.demo.Filter.repository;

import com.example.demo.Filter.domain.ChatFilterHistory;
import com.example.demo.Filter.domain.Filter;
import com.example.demo.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatFilterHistoryRepository extends JpaRepository<ChatFilterHistory, Long> {

    // 채팅 필터 히스토리에 이미 사용 중인지 확인
    boolean existsByChatRoomAndFilter(ChatRoom chatRoom, Filter filter);

    Optional<ChatFilterHistory> findByChatRoom_ChatRoomId(Long chatRoomId);

    @Query("""
    SELECT cfh
    FROM ChatFilterHistory cfh
    JOIN FETCH cfh.filter f
    JOIN FETCH f.region
    WHERE cfh.chatRoom.chatRoomId = :chatRoomId
    """)
    Optional<ChatFilterHistory> findByChatRoom_ChatRoomIdFetchFilterAndRegion(@Param("chatRoomId") Long chatRoomId);

}
