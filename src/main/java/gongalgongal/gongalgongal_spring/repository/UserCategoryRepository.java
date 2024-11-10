package gongalgongal.gongalgongal_spring.repository;

import gongalgongal.gongalgongal_spring.model.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategory, String> {
    // 추가적인 쿼리 메서드 정의 가능
}
