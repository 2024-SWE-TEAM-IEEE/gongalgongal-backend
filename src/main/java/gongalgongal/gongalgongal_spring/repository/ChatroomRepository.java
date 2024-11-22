package gongalgongal.gongalgongal_spring.repository;

import gongalgongal.gongalgongal_spring.model.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByNoticeId(Long noticeId);
}