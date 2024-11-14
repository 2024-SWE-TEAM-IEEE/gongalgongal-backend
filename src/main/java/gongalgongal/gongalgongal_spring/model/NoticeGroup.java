package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
public class NoticeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private Long adminId;

    private String groupName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private String shareUrl;

    private String crawlSiteUrl;

    // Many-to-Many 관계 설정
    @ManyToMany
    @JoinTable(
            name = "group_category", // 연결 테이블 이름
            joinColumns = @JoinColumn(name = "group_id"), // NoticeGroup의 외래키
            inverseJoinColumns = @JoinColumn(name = "category_id") // GroupCategory의 외래키
    )
    private Set<Category> groupCategory; // Set으로 변경하여 중복 방지

    // 기본 생성자
    public NoticeGroup() {}

    // 생성자
    public NoticeGroup(Long adminId, String groupName, String shareUrl, String crawlSiteUrl) {
        this.adminId = adminId;
        this.groupName = groupName;
        this.shareUrl = shareUrl;
        this.crawlSiteUrl = crawlSiteUrl;
    }
}
