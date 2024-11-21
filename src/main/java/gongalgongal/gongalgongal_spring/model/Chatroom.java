package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private Long noticeId;

    @ManyToMany(mappedBy = "chatrooms")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessages = new ArrayList<>(); // 채팅 메시지 저장

    // 기본 생성자
    public Chatroom() {}

    // noticeId만 받는 생성자
    public Chatroom(Long noticeId) {
        this.noticeId = noticeId;
    }

}