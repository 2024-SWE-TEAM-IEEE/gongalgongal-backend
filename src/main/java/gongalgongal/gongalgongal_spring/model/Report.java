package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatMessage chatMessage; // 신고된 메시지

    @ManyToOne
    @JoinColumn(name = "reporting_user_id", nullable = false)
    private User reportingUser; // 신고한 사용자

    private String reason; // 신고 사유

    private LocalDateTime createdAt; // 신고 일시
}
