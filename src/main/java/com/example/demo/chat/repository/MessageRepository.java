package com.example.demo.chat.repository;

import com.example.demo.chat.domain.ChatRoom;
import com.example.demo.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

//    @Modifying
//    @Query("UPDATE Message m SET m.deletedAt = :now WHERE m.chatRoom.chatRoomId = :chatRoomId")
//    void softDeleteMessages(@Param("chatRoomId") Long chatRoomId, @Param("now") LocalDateTime now);

    // 로그인한 유저가 특정 채팅방을 조회할 때 삭제되지 않은 메세지만 조회
    List<Message> findByChatRoom_ChatRoomIdAndDeletedAtIsNull(Long chatRoomId);

    // 사용자가 검색한 검색어와 매칭되는 채팅방 내용중에서 가장 최신의 데이터 담기
    Optional<Message>  findTopByChatRoomAndContentContainingIgnoreCaseOrderByMessageIdDesc(ChatRoom chatRoom, String searchText);


    // 검색어가 채팅방에서 채팅한 내용과 동일할 때 조회
    //boolean existsByChatRoomAndContentContainingIgnoreCase(ChatRoom chatRoom, String searchText);

    //Message findTop1ByChatRoom_ChatRoomIdAndSenderTypeOrderByCreatedAtDesc(Long chatRoomId, SenderType senderType);
}
