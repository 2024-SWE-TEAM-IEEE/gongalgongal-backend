package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserGroup {

    @Id
    private Long userId;

    @Id
    private Long groupId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // 기본 생성자
    public UserGroup() {}

    // 생성자
    public UserGroup(Long userId, Long groupId, UserRole role) {
        this.userId = userId;
        this.groupId = groupId;
        this.role = role;
    }
}

// ENUM 타입 정의
public enum UserRole {
    Admin, Member
}
