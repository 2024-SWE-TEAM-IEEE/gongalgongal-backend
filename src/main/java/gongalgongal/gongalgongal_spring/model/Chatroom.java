package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private Long noticeId;

    // 기본 생성자
    public Chatroom() {}

    // 생성자
    public Chatroom(Long noticeId) {
        this.noticeId = noticeId;
    }
}
