package gongalgongal.gongalgongal_spring.repository;

import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.model.UserNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserNoticeRepository extends JpaRepository<UserNotice, Long> {
    List<UserNotice> findByUser(User user);

    @Query("SELECT un FROM UserNotice un " +
            "JOIN un.user u " +
            "WHERE u.email = :email AND un.notice.noticeId = :noticeId")
    Optional<UserNotice> findByUserEmailAndNoticeId(@Param("email") String email, @Param("noticeId") Long noticeId);
}
