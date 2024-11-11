package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Entity
@Data
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userGroupId;

    private Long userId;
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

