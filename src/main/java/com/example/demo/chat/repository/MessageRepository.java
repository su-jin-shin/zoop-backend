package com.example.demo.chat.repository;

import com.example.demo.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Modifying
    @Query("UPDATE Message m SET m.deletedAt = :now WHERE m.chatRoom.chatRoomId = :chatRoomId")
    void softDeleteMessages(@Param("chatRoomId") Long chatRoomId, @Param("now") LocalDateTime now);

}
