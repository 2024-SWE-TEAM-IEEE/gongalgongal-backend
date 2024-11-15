package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.NoticesResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticesDetailResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeStoreResponseDto;

import gongalgongal.gongalgongal_spring.model.Notice;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.model.UserNotice;

import gongalgongal.gongalgongal_spring.repository.UserNoticeRepository;
import gongalgongal.gongalgongal_spring.repository.UserRepository;
import gongalgongal.gongalgongal_spring.repository.NoticeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class NoticeService {

    private final UserNoticeRepository userNoticeRepository;
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeService(UserNoticeRepository userNoticeRepository,
                         UserRepository userRepository,
                         NoticeRepository noticeRepository) {
        this.userNoticeRepository = userNoticeRepository;
        this.userRepository = userRepository;
        this.noticeRepository = noticeRepository;
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

    /* 공지 상세 조회 */
    public NoticesDetailResponseDto getNoticeDetail(Long noticeId, Authentication authentication) {
        if (noticeId == null || noticeId <= 0) {
            throw new IllegalArgumentException("Invalid notice ID provided.");
        }

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("Notice not found with ID: " + noticeId));

        String userEmail = authentication.getName();

        Optional<UserNotice> userNoticeOpt = userNoticeRepository.findByUserEmailAndNoticeId(userEmail, noticeId);

        boolean isStored = userNoticeOpt.map(UserNotice::getIsStored).orElse(false);
        boolean isStarred = userNoticeOpt.map(UserNotice::getIsStarred).orElse(false);

        NoticesDetailResponseDto.NoticeDetail responseData = new NoticesDetailResponseDto.NoticeDetail(
                notice.getNoticeId(),
                notice.getTitle(),
                notice.getCategories().stream()
                        .map(category -> new NoticesDetailResponseDto.Category(
                                category.getCategoryId(),
                                category.getCategoryName()
                        ))
                        .collect(Collectors.toList()),
                notice.getContent(),
                notice.getAuthor(),
                notice.getCreatedAt(),
                notice.getUrl(),
                isStored,
                isStarred,
                false // [TODO]: 이 부분은 공지 대화방 개발되면 수정 필요
        );

        NoticesDetailResponseDto.Status status = new NoticesDetailResponseDto.Status(
                "success",
                "Notice fetched successfully"
        );

        return new NoticesDetailResponseDto(status, responseData);
    }

    public NoticeStoreResponseDto storeNotice(Long noticeId, Authentication authentication) {
        // 사용자 이메일 추출
        String userEmail = authentication.getName();

        // 사용자 조회
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            return new NoticeStoreResponseDto(
                    new NoticeStoreResponseDto.Status("failed", "User not found"));
        }
        User user = userOpt.get();

        // 공지사항 조회
        Optional<Notice> noticeOpt = noticeRepository.findById(noticeId);
        if (noticeOpt.isEmpty()) {
            return new NoticeStoreResponseDto(
                    new NoticeStoreResponseDto.Status("failed", "Notice not found"));
        }
        Notice notice = noticeOpt.get();

        // UserNotice 존재 여부 확인
        Optional<UserNotice> userNoticeOpt = userNoticeRepository.findByUserAndNotice(user, notice);
        UserNotice userNotice;

        if (userNoticeOpt.isPresent()) {
            userNotice = userNoticeOpt.get();
        } else {
            // UserNotice 새로 생성
            userNotice = new UserNotice(user, notice, false, true);
        }

        // 보관 상태 업데이트
        userNotice.setIsStored(true);
        userNoticeRepository.save(userNotice);

        return new NoticeStoreResponseDto(
                new NoticeStoreResponseDto.Status("success", "Notice successfully stored"));
    }

}
