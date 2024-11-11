package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    private String title;

    private String author;

    private String url;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // 기본 생성자
    public Notice() {}

    // 생성자
    public Notice(String title, String author, String url) {
        this.title = title;
        this.author = author;
        this.url = url;
    }
}
