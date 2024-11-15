package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
public class NoticeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private Long adminId;

    private String groupName;

    private String description;

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

    // NoticeGroup과 UserGroup 간의 일대다 관계
    @OneToMany(mappedBy = "noticeGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UserGroup> userGroups = new HashSet<>(); // 그룹에 속한 사용자 목록

    // 기본 생성자
    public NoticeGroup() {}

    // 생성자
    public NoticeGroup(Long adminId, String groupName, String description, String shareUrl, String crawlSiteUrl) {
        this.adminId = adminId;
        this.groupName = groupName;
        this.description = description;
        this.shareUrl = shareUrl;
        this.crawlSiteUrl = crawlSiteUrl;
    }
}
