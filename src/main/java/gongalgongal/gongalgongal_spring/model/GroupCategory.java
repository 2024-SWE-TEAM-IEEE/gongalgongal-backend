package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class GroupCategory {

    @Id
    private Long groupId;

    @Id
    private Long categoryId;

    // 기본 생성자
    public GroupCategory() {}

    // 생성자
    public GroupCategory(Long groupId, Long categoryId) {
        this.groupId = groupId;
        this.categoryId = categoryId;
    }
}