package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatroom;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String content;

    private LocalDateTime createdAt;

    public ChatMessage(Chatroom chatroom, User user, String content, LocalDateTime createdAt) {
        this.chatroom = chatroom;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
    }
}
