package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateRequestDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupJoinResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupsResponseDto;

import gongalgongal.gongalgongal_spring.model.NoticeGroup;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.model.Category;

import gongalgongal.gongalgongal_spring.repository.UserRepository;
import gongalgongal.gongalgongal_spring.repository.CategoryRepository;
import gongalgongal.gongalgongal_spring.repository.NoticeGroupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoticeGroupService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final NoticeGroupRepository noticeGroupRepository;

    @Autowired
    public NoticeGroupService(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            NoticeGroupRepository noticeGroupRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.noticeGroupRepository = noticeGroupRepository;
    }

    /* 공지 그룹 생성 */
    public NoticeGroupCreateResponseDto createNoticeGroup(NoticeGroupCreateRequestDto request, Authentication authentication) {

        // 1. Authentication 객체에서 adminId 추출
        String email = authentication.getName(); // 이메일 가져오기
        Long adminId = userRepository.findByEmail(email)
                .map(User::getId) // 유저 ID 추출
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // 2. Category 매핑 (카테고리가 비어 있어도 처리 가능)
        List<Long> categoryIds = request.getGroupCategory().stream()
                .map(Integer::longValue) // Integer → Long 변환
                .collect(Collectors.toList());
        Set<Category> categories = categoryIds == null || categoryIds.isEmpty()
                ? Collections.emptySet() // 빈 Set 처리
                : new HashSet<>(categoryRepository.findAllById(categoryIds)); // ID로 Category 조회

        // 3. NoticeGroup 생성
        NoticeGroup noticeGroup = new NoticeGroup(
                adminId,
                request.getGroupName(),
                null, // 공유 URL은 나중에 생성
                request.getCrawlSiteUrl()
        );
        noticeGroup.setGroupCategory(categories); // Category와 매핑
        NoticeGroup savedNoticeGroup = noticeGroupRepository.save(noticeGroup);

        // 4. 저장된 그룹 ID를 기반으로 공유 URL 생성
        String shareUrl = generateShareUrl(savedNoticeGroup.getGroupId(), savedNoticeGroup.getGroupName());
        savedNoticeGroup.setShareUrl(shareUrl);

        // 5. 공유 URL 업데이트
        noticeGroupRepository.save(savedNoticeGroup);

        // 6. 응답 DTO 생성
        NoticeGroupCreateResponseDto.Status status = new NoticeGroupCreateResponseDto.Status("success", "Group created successfully");
        NoticeGroupCreateResponseDto.GroupCreationData data = new NoticeGroupCreateResponseDto.GroupCreationData(
                true,
                savedNoticeGroup.getGroupId(),
                shareUrl
        );

        return new NoticeGroupCreateResponseDto(status, data);
    }

    /* 공유 URL 생성 */
    private String generateShareUrl(Long groupId, String groupName) {
        return "https://your-service.com/groups/" + groupName.replace(" ", "-") + "-" + groupId;
    }


    /* 참가한 공지 그룹 리스트 조회 */
    public NoticeGroupsResponseDto getJoinedNoticeGroups(Long userId) {
        // 실제 데이터베이스 조회 대신 예시 데이터를 생성
        List<NoticeGroupsResponseDto.Group> groups = searchJoinedNoticeGroups(userId);

        NoticeGroupsResponseDto.GroupListData data = new NoticeGroupsResponseDto.GroupListData(groups);
        return new NoticeGroupsResponseDto(new NoticeGroupsResponseDto.Status("success", "조회 성공"), data);
    }

    // [[TODO]]
    private List<NoticeGroupsResponseDto.Group> searchJoinedNoticeGroups(long userId) {
        // 실제 userId로 그룹리스트 찾는 로직 구현
        return List.of(
                new NoticeGroupsResponseDto.Group(
                        "group123",
                        "기술 공지 그룹",
                        1,
                        "https://www.dongguk.edu/article/JANGHAKNOTICE/list",
                        List.of(1, 2),
                        "기술 관련 공지 사항을 공유하는 그룹입니다.",
                        "https://example.com/invite/group123",
                        List.of(
                                new NoticeGroupsResponseDto.Member(1, "사용자A"),
                                new NoticeGroupsResponseDto.Member(2, "사용자B"),
                                new NoticeGroupsResponseDto.Member(3, "사용자C")
                        )
                ),
                new NoticeGroupsResponseDto.Group(
                        "group456",
                        "예술 공지 그룹",
                        2,
                        "https://www.dongguk.edu/article/JANGHAKNOTICE/list",
                        List.of(3, 4),
                        "예술 관련 공지 사항을 공유하는 그룹입니다.",
                        "https://example.com/invite/group456",
                        List.of(
                                new NoticeGroupsResponseDto.Member(1, "사용자A"),
                                new NoticeGroupsResponseDto.Member(2, "사용자B"),
                                new NoticeGroupsResponseDto.Member(3, "사용자C")
                        )
                )
        );
    }



    /* 공지 그룹 참가 */
    public NoticeGroupJoinResponseDto joinNoticeGroup(Long groupId) {
        // 그룹 ID가 유효한지 확인
//        NoticeGroup noticeGroup = noticeGroupRepository.findById(groupId)
//                .orElseThrow(() -> new NoSuchElementException("Group not found"));

        // 이미 참가한 상태인지 확인
        if (isUserAlreadyJoined(groupId)) {
            throw new IllegalStateException("User already joined the group");
        }

        // 참가 로직 처리
        joinUserToGroup(groupId);

        // 성공 응답 생성
        return new NoticeGroupJoinResponseDto(new NoticeGroupJoinResponseDto.Status("success", "Joined group successfully"));
    }

    // [[TODO]]
    private boolean isUserAlreadyJoined(Long groupId) {
        // 이미 참가한 그룹인지 확인하는 로직 (예: 특정 사용자 ID가 그룹에 이미 존재하는지 검사)
        return false; // 예제 코드에서는 false로 설정
    }

    // [[TODO]]
    private void joinUserToGroup(Long groupId) {
        // 실제 그룹 참가 로직 구현
    }




}
