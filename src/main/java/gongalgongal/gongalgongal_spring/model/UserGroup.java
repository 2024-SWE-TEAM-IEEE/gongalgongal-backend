package gongalgongal.gongalgongal_spring.model;

import gongalgongal.gongalgongal_spring.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Entity
@Data
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userGroupId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User와 다대일 관계

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
    private NoticeGroup noticeGroup; // NoticeGroup과 다대일 관계

    @Enumerated(EnumType.STRING)
    private UserRole role; // 역할: ADMIN 또는 MEMBER

    // 기본 생성자
    public UserGroup() {}

    // 생성자
    public UserGroup(User user, NoticeGroup noticeGroup, UserRole role) {
        this.user = user;
        this.noticeGroup = noticeGroup;
        this.role = role;
    }
}


