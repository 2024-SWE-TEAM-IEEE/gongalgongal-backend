package gongalgongal.gongalgongal_spring.repository;

import gongalgongal.gongalgongal_spring.model.UserGroup;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.model.NoticeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    boolean existsByUserAndNoticeGroup(User user, NoticeGroup noticeGroup);
}
