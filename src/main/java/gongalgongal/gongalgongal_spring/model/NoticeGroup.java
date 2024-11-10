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
public class NoticeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private Long adminId;

    private String groupName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private String share_url;

    private String crawl_site_url;

    // 기본 생성자
    public NoticeGroup() {}

    // 생성자
    public NoticeGroup(Long adminId, String groupName, String share_url, String crawl_site_url) {
        this.adminId = adminId;
        this.groupName = groupName;
        this.share_url = share_url;
        this.crawl_site_url = crawl_site_url;
    }
}
