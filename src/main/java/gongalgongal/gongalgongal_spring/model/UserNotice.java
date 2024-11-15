package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Entity
@Data
public class UserNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNoticeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notice_id", referencedColumnName = "noticeId", nullable = false)
    private Notice notice;

    private Boolean isStarred;

    private Boolean isStored;

    // 기본 생성자
    public UserNotice() {}

    // 생성자
    public UserNotice(User user, Notice notice, Boolean isStarred, Boolean isStored) {
        this.user = user;
        this.notice = notice;
        this.isStarred = isStarred;
        this.isStored = isStored;
    }
}
