package gongalgongal.gongalgongal_spring.repository;

import gongalgongal.gongalgongal_spring.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 추가적인 쿼리 메서드 정의 가능
}
