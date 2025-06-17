package com.example.demo.chat.repository;

import com.example.demo.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Modifying
    @Query("UPDATE Message m SET m.deletedAt = :now WHERE m.chatRoom.chatRoomId = :chatRoomId")
    void softDeleteMessages(@Param("chatRoomId") Long chatRoomId, @Param("now") LocalDateTime now);

    // 로그인한 유저가 특정 채팅방을 조회할 때 삭제되지 않은 메세지만 조회
    List<Message> findByChatRoom_ChatRoomIdAndDeletedAtIsNull(Long chatRoomId);
}
