package gongalgongal.gongalgongal_spring.repository;

import gongalgongal.gongalgongal_spring.model.ChatMessage;

import gongalgongal.gongalgongal_spring.model.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByNoticeId(Long noticeId);

    @Query("SELECT m FROM ChatMessage m WHERE m.chatroom.id = :chatroomId ORDER BY m.createdAt ASC")
    List<ChatMessage> findByChatroomId(@Param("chatroomId") Long chatroomId);
}