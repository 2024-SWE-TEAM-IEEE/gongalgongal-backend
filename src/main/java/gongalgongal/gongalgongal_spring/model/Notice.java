package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    private String title;

    private String author;

    private String url;

    private Timestamp createdAt;

    // 기본 생성자
    public Notice() {}

    // 생성자
    public Notice(String title, String author, String url, Timestamp createdAt) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.createdAt = createdAt;
    }
}
