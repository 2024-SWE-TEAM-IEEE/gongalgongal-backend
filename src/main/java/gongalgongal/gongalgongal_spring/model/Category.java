package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String categoryName;

    private String description;

    // 기본 생성자
    public Category() {}

    // 생성자
    public Category(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }
}
