package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    private Long userId;

    private Long chatId;

    private String content;

    private Timestamp createdAt;

    // 기본 생성자
    public ChatMessage() {}

    // 생성자
    public ChatMessage(Long userId, Long chatId, String content, Timestamp createdAt) {
        this.userId = userId;
        this.chatId = chatId;
        this.content = content;
        this.createdAt = createdAt;
    }
}

