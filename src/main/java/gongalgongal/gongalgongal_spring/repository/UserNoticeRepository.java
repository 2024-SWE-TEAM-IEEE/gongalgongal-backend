package gongalgongal.gongalgongal_spring.repository;

import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.model.UserNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserNoticeRepository extends JpaRepository<UserNotice, Long> {
    List<UserNotice> findByUser(User user);
}
