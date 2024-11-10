package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserCategory {

    @Id
    private Long categoryId;

    @Id
    private Long userId;

    // 기본 생성자
    public UserCategory() {}

    // 생성자
    public UserCategory(Long categoryId, Long userId) {
        this.categoryId = categoryId;
        this.userId = userId;
    }
}
