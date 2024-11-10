package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserChatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userChatroomId;

    private Long userId;
    private Long chatId;

    // 기본 생성자
    public UserChatroom() {}

    // 생성자
    public UserChatroom(Long userId, Long chatId) {
        this.userId = userId;
        this.chatId = chatId;
    }
}

