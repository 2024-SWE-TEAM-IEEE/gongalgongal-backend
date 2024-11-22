package gongalgongal.gongalgongal_spring.repository;

import gongalgongal.gongalgongal_spring.model.ChatMessage;
import gongalgongal.gongalgongal_spring.model.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatroom = :chatroom")
    List<ChatMessage> findByChatroom(@Param("chatroom") Chatroom chatroom);
}