package gongalgongal.gongalgongal_spring.repository;

import gongalgongal.gongalgongal_spring.model.NoticeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeGroupRepository extends JpaRepository<NoticeGroup, String> {
    // 추가적인 쿼리 메서드 정의 가능
}
