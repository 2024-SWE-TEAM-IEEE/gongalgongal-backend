package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.NoticesResponseDto;

import gongalgongal.gongalgongal_spring.model.Notice;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.model.UserNotice;

import gongalgongal.gongalgongal_spring.repository.UserNoticeRepository;
import gongalgongal.gongalgongal_spring.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {

    private final UserNoticeRepository userNoticeRepository;
    private final UserRepository userRepository;

    @Autowired
    public NoticeService(UserNoticeRepository userNoticeRepository,
                         UserRepository userRepository) {
        this.userNoticeRepository = userNoticeRepository;
        this.userRepository = userRepository;
    }

    public NoticesResponseDto getNotices(Authentication authentication) {
        // 1. 사용자 인증 정보로 User 가져오기
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // 2. 사용자의 공지사항 조회
        List<UserNotice> userNotices = userNoticeRepository.findByUser(user);

        // 3. 공지사항 데이터 변환
        List<NoticesResponseDto.Notice> notices = userNotices.stream()
                .map(userNotice -> {
                    Notice notice = userNotice.getNotice();
                    return new NoticesResponseDto.Notice(
                            notice.getNoticeId(),
                            notice.getTitle(),
                            notice.getCategories().stream()
                                    .map(category -> new NoticesResponseDto.Category(
                                            category.getCategoryId(),
                                            category.getCategoryName()
                                    ))
                                    .collect(Collectors.toList()),
                            notice.getAuthor(),
                            notice.getCreatedAt(),
                            userNotice.getIsStored(),
                            userNotice.getIsStarred()
                    );
                })
                .collect(Collectors.toList());

        // 4. 응답 생성
        NoticesResponseDto.NoticeListData data = new NoticesResponseDto.NoticeListData(notices);
        return new NoticesResponseDto(new NoticesResponseDto.Status("success", "공지사항 조회 성공"), data);
    }
}
