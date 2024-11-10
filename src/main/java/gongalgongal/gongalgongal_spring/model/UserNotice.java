package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserNotice {

    @Id
    private Long userId;

    @Id
    private Long noticeId;

    private Boolean isStarred;

    private Boolean isStored;

    // 기본 생성자
    public UserNotice() {}

    // 생성자
    public UserNotice(Long userId, Long noticeId, Boolean isStarred, Boolean isStored) {
        this.userId = userId;
        this.noticeId = noticeId;
        this.isStarred = isStarred;
        this.isStored = isStored;
    }
}
