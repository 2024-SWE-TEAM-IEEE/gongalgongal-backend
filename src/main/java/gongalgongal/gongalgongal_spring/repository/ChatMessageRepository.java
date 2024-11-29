package gongalgongal.gongalgongal_spring.repository;

import gongalgongal.gongalgongal_spring.model.ChatMessage;
import gongalgongal.gongalgongal_spring.model.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT m FROM ChatMessage m WHERE m.chatroom.id = :chatroomId ORDER BY m.createdAt ASC")
    List<ChatMessage> findByChatroomId(@Param("chatroomId") Long chatroomId);
}