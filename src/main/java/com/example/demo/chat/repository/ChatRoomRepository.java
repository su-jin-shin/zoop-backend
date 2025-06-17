package com.example.demo.chat.repository;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 채팅방 삭제
    @Modifying
    @Query("UPDATE ChatRoom c SET c.isDeleted = true, c.deletedAt = :now WHERE c.chatRoomId = :chatRoomId")
    void softDeleteChatRoom(@Param("chatRoomId") Long chatRoomId, @Param("now")LocalDateTime now);
    
    // 채팅방 목록 조회
    List<ChatRoom> findByUserInfoAndIsDeletedFalseOrderByLastMessageAtDesc(UserInfo userInfo);

    // 특정 유저의 특정 채팅방 조회
    ChatRoom findByChatRoomIdAndUserInfo(Long chatRoomId, UserInfo userInfo);

}
