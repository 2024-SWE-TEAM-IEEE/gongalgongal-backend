package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateRequestDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupJoinResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupsResponseDto;

import gongalgongal.gongalgongal_spring.model.NoticeGroup;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.model.UserGroup;
import gongalgongal.gongalgongal_spring.model.Category;
import gongalgongal.gongalgongal_spring.model.UserRole;

import gongalgongal.gongalgongal_spring.repository.UserRepository;
import gongalgongal.gongalgongal_spring.repository.UserGroupRepository;
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
    private final UserGroupRepository userGroupRepository;
    private final CategoryRepository categoryRepository;
    private final NoticeGroupRepository noticeGroupRepository;

    @Autowired
    public NoticeGroupService(
            UserRepository userRepository,
            UserGroupRepository userGroupRepository,
            CategoryRepository categoryRepository,
            NoticeGroupRepository noticeGroupRepository) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.categoryRepository = categoryRepository;
        this.noticeGroupRepository = noticeGroupRepository;
    }

    /* 공지 그룹 생성 */
    /* 공지 그룹 생성 */
    public NoticeGroupCreateResponseDto createNoticeGroup(NoticeGroupCreateRequestDto request, Authentication authentication) {

        // 1. Authentication 객체에서 adminId 추출
        String email = authentication.getName(); // 이메일 가져오기
        User user = userRepository.findByEmail(email)
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
                user.getId(),
                request.getGroupName(),
                null, // 공유 URL은 나중에 생성
                request.getCrawlSiteUrl()
        );
        noticeGroup.setGroupCategory(categories); // Category와 매핑
        NoticeGroup savedNoticeGroup = noticeGroupRepository.save(noticeGroup);

        // 4. 저장된 그룹 ID를 기반으로 공유 URL 생성
        String shareUrl = generateShareUrl(savedNoticeGroup.getGroupId(), savedNoticeGroup.getGroupName());
        savedNoticeGroup.setShareUrl(shareUrl);
        noticeGroupRepository.save(savedNoticeGroup); // 공유 URL 업데이트

        // 5. 생성자를 UserGroup에 Admin으로 추가
        UserGroup userGroup = new UserGroup(user, savedNoticeGroup, UserRole.Admin); // 역할: ADMIN
        userGroupRepository.save(userGroup);

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
    public NoticeGroupJoinResponseDto joinNoticeGroup(Long groupId, Authentication authentication) {
        // 1. Authentication 객체에서 유저 이메일 가져오기
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // 2. 그룹 존재 여부 확인
        NoticeGroup noticeGroup = noticeGroupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("Group not found with ID: " + groupId));

        // 3. 이미 참가한 상태인지 확인
        if (isUserAlreadyJoined(user, noticeGroup)) {
            throw new IllegalStateException("User already joined the group");
        }

        // 4. 참가 로직 처리
        joinUserToGroup(user, noticeGroup);

        // 5. 성공 응답 생성
        return new NoticeGroupJoinResponseDto(
                new NoticeGroupJoinResponseDto.Status("success", "Joined group successfully")
        );
    }

    /* 이미 참가한 그룹인지 확인 */
    private boolean isUserAlreadyJoined(User user, NoticeGroup noticeGroup) {
        return userGroupRepository.existsByUserAndNoticeGroup(user, noticeGroup);
    }

    /* 그룹 참가 로직 구현 */
    private void joinUserToGroup(User user, NoticeGroup noticeGroup) {
        // UserGroup 생성 및 저장
        UserGroup userGroup = new UserGroup(user, noticeGroup, UserRole.Member); // 기본 역할: MEMBER
        userGroupRepository.save(userGroup);
    }

}
