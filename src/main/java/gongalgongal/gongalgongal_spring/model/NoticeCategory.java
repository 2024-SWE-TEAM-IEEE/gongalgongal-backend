package gongalgongal.gongalgongal_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class NoticeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeCategoryId;

    private Long noticeId;
    private Long categoryId;

    // 기본 생성자
    public NoticeCategory() {}

    // 생성자
    public NoticeCategory(Long noticeId, Long categoryId) {
        this.noticeId = noticeId;
        this.categoryId = categoryId;
    }
}
