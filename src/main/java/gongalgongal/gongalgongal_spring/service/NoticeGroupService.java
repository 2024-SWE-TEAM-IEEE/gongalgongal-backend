package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateRequestDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupJoinResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupsResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupLeaveResponseDto;

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
import org.springframework.transaction.annotation.Transactional;

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
                request.getDescription(),
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
    public NoticeGroupsResponseDto getJoinedNoticeGroups(Authentication authentication) {
        // 1. Authentication 객체에서 사용자 이메일 추출
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // 2. 참가한 공지 그룹 리스트 조회
        List<NoticeGroupsResponseDto.Group> groups = searchJoinedNoticeGroups(user.getId());

        // 3. 응답 생성
        NoticeGroupsResponseDto.GroupListData data = new NoticeGroupsResponseDto.GroupListData(groups);
        return new NoticeGroupsResponseDto(new NoticeGroupsResponseDto.Status("success", "조회 성공"), data);
    }

    /* 실제 userId로 그룹리스트 찾는 로직 구현 */
    @Transactional
    private List<NoticeGroupsResponseDto.Group> searchJoinedNoticeGroups(Long userId) {
        // 1. UserGroup 테이블에서 userId로 NoticeGroup 조회
        List<UserGroup> userGroups = userGroupRepository.findByUser_Id(userId);

        // 2. 조회한 UserGroup 데이터로 NoticeGroupsResponseDto.Group 객체 생성
        return userGroups.stream()
                .map(userGroup -> {
                    NoticeGroup noticeGroup = userGroup.getNoticeGroup();

                    return new NoticeGroupsResponseDto.Group(
                            noticeGroup.getGroupId(),
                            noticeGroup.getGroupName(),
                            noticeGroup.getAdminId(),
                            noticeGroup.getCrawlSiteUrl(),
                            noticeGroup.getGroupCategory().stream()
                                    .map(Category::getCategoryId)
                                    .collect(Collectors.toList()),
                            noticeGroup.getDescription(),
                            noticeGroup.getShareUrl(),
                            noticeGroup.getUserGroups().stream()
                                    .map(member -> {
                                        User user = member.getUser();
                                        if (user == null) {
                                            throw new IllegalStateException("User data is missing for member.");
                                        }
                                        return new NoticeGroupsResponseDto.Member(user.getId(), user.getName());
                                    })
                                    .collect(Collectors.toList())
                    );
                })
                .collect(Collectors.toList());
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


    /* 공지 그룹 탈퇴 */
    @Transactional
    public NoticeGroupLeaveResponseDto leaveNoticeGroup(Long groupId, Authentication authentication) {
        // 1. Authentication 객체에서 사용자 이메일 추출
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // 2. 그룹 존재 여부 확인
        NoticeGroup noticeGroup = noticeGroupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("Group not found with ID: " + groupId));

        // 3. UserGroup 조회
        UserGroup userGroup = userGroupRepository.findByUserAndNoticeGroup(user, noticeGroup)
                .orElseThrow(() -> new NoSuchElementException("User is not a member of the specified group"));

        // 4. 관리자 권한 체크
        if (userGroup.getRole() == UserRole.Admin) {
            // 그룹 내 다른 멤버 조회
            List<UserGroup> otherMembers = userGroupRepository.findByNoticeGroupAndUserNot(noticeGroup, user);
            if (otherMembers.isEmpty()) {
                // 그룹 내 다른 멤버가 없는 경우 그룹 삭제
                noticeGroupRepository.delete(noticeGroup);
            } else {
                // 그룹 내 다른 멤버가 있는 경우 새 관리자를 랜덤으로 지정
                UserGroup newAdmin = otherMembers.get(new Random().nextInt(otherMembers.size()));
                newAdmin.setRole(UserRole.Admin);
                userGroupRepository.save(newAdmin);
            }
        }

//        // 5. 관련 데이터에 "탈퇴한 사용자"로 표시
//        // 예: 공지사항이나 댓글을 작성한 데이터에서 작성자 정보를 "탈퇴한 사용자"로 업데이트
//        noticeGroupRepository.updateCreatorToLeftUser(user.getId(), groupId);

        // 6. UserGroup 엔트리 삭제
        userGroupRepository.delete(userGroup);

        // 7. 응답 생성
        return new NoticeGroupLeaveResponseDto(
                new NoticeGroupLeaveResponseDto.Status("success", "You have successfully left the group.")
        );
    }
}
