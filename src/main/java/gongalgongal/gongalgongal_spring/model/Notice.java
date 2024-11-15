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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "notice_category",
            joinColumns = @JoinColumn(name = "notice_id", referencedColumnName = "noticeId"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "categoryId")
    )
    private Set<Category> categories = new HashSet<>();

    // Notice와 UserNotice 간의 일대다 관계
    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UserNotice> userNotices = new HashSet<>(); // 그룹에 속한 사용자 목록

    // 기본 생성자
    public Notice() {}

    // 생성자
    public Notice(String title, String author, String url) {
        this.title = title;
        this.author = author;
        this.url = url;
    }
}
