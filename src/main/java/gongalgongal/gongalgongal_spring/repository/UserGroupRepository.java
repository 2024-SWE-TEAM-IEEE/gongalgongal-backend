package gongalgongal.gongalgongal_spring.repository;

import gongalgongal.gongalgongal_spring.model.UserGroup;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.model.NoticeGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    boolean existsByUserAndNoticeGroup(User user, NoticeGroup noticeGroup);

    Optional<UserGroup> findByUserAndNoticeGroup(User user, NoticeGroup noticeGroup);

    List<UserGroup> findByNoticeGroupAndUserNot(NoticeGroup noticeGroup, User user);

    @Query("SELECT ug FROM UserGroup ug JOIN FETCH ug.user JOIN FETCH ug.noticeGroup WHERE ug.user.id = :userId")
    List<UserGroup> findByUser_Id(@Param("userId") Long userId);

    void deleteByNoticeGroup(NoticeGroup noticeGroup);
}
