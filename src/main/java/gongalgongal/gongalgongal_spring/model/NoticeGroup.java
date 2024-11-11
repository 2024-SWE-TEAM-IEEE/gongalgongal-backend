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

    private Long userId;

    private String groupName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // 기본 생성자
    public NoticeGroup() {}

    // 생성자
    public NoticeGroup(Long userId, String groupName) {
        this.userId = userId;
        this.groupName = groupName;
    }
}
