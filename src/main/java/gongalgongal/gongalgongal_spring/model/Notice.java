package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.*;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.*;

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

    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Notice와 UserNotice 간의 일대다 관계
    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private ArrayList<UserNotice> userNotices = new ArrayList<>(); // 그룹에 속한 사용자 목록

    // 기본 생성자
    public Notice() {}

    // 생성자
    public Notice(String title, String author, String url, String content) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.content = content;
    }
}
