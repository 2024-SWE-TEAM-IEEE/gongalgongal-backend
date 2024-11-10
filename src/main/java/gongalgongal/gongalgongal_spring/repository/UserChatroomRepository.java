package gongalgongal.gongalgongal_spring.repository;

import gongalgongal.gongalgongal_spring.model.UserChatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatroomRepository extends JpaRepository<UserChatroom, String> {
    // 추가적인 쿼리 메서드 정의 가능
}
