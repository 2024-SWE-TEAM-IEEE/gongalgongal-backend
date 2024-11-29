package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateRequestDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupJoinResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupsResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupLeaveResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupDeleteResponseDto;

import gongalgongal.gongalgongal_spring.model.NoticeGroup;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.model.UserGroup;
import gongalgongal.gongalgongal_spring.model.Category;
import gongalgongal.gongalgongal_spring.model.UserRole;
import gongalgongal.gongalgongal_spring.model.Notice;
import gongalgongal.gongalgongal_spring.model.UserNotice;

import gongalgongal.gongalgongal_spring.repository.UserRepository;
import gongalgongal.gongalgongal_spring.repository.UserGroupRepository;
import gongalgongal.gongalgongal_spring.repository.CategoryRepository;
import gongalgongal.gongalgongal_spring.repository.NoticeGroupRepository;
import gongalgongal.gongalgongal_spring.repository.NoticeRepository;
import gongalgongal.gongalgongal_spring.repository.UserNoticeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException; // 데이터 무결성 예외

import java.lang.IllegalArgumentException; // 잘못된 인자 예외
import java.lang.RuntimeException; // 런타임 예외
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoticeGroupService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final CategoryRepository categoryRepository;
    private final NoticeGroupRepository noticeGroupRepository;
    private final NoticeRepository noticeRepository;
    private final UserNoticeRepository userNoticeRepository;

    @Autowired
    public NoticeGroupService(
            UserRepository userRepository,
            UserGroupRepository userGroupRepository,
            CategoryRepository categoryRepository,
            NoticeGroupRepository noticeGroupRepository,
            NoticeRepository noticeRepository,
            UserNoticeRepository userNoticeRepository) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.categoryRepository = categoryRepository;
        this.noticeGroupRepository = noticeGroupRepository;
        this.noticeRepository = noticeRepository;
        this.userNoticeRepository = userNoticeRepository;
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
        return "/tab/group/details/" + groupId;
    }


    /* 전체 공지 그룹 리스트 조회 */
    public NoticeGroupsResponseDto getAllNoticeGroupsWithParticipantStatus(Authentication authentication) {
        // 1. Authentication 객체에서 사용자 이메일 추출
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // 2. 전체 공지 그룹 리스트 조회
        List<NoticeGroupsResponseDto.Group> groups = searchAllNoticeGroups(user.getId());

        // 3. 응답 생성
        NoticeGroupsResponseDto.GroupListData data = new NoticeGroupsResponseDto.GroupListData(groups);
        return new NoticeGroupsResponseDto(new NoticeGroupsResponseDto.Status("success", "조회 성공"), data);
    }

    /* 실제 userId로 전체 그룹 리스트와 참가 여부 플래그를 찾는 로직 구현 */
    @Transactional
    private List<NoticeGroupsResponseDto.Group> searchAllNoticeGroups(Long userId) {
        // 1. 사용자 ID로 User 객체 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // 2. 전체 NoticeGroup 조회
        List<NoticeGroup> allNoticeGroups = noticeGroupRepository.findAll();

        // 3. 사용자가 참가한 그룹 ID 조회
        List<Long> participantGroupIds = userGroupRepository.findByUser_Id(userId).stream()
                .map(userGroup -> userGroup.getNoticeGroup().getGroupId())
                .collect(Collectors.toList());

        // 4. NoticeGroup 데이터를 NoticeGroupsResponseDto.Group 객체로 변환
        return allNoticeGroups.stream()
                .map(noticeGroup -> {
                    // 그룹의 모든 멤버 조회 (현재 사용자 포함)
                    List<UserGroup> allMembers = userGroupRepository.findByNoticeGroup(noticeGroup);

                    // 멤버 리스트 생성
                    List<NoticeGroupsResponseDto.Member> members = allMembers.stream()
                            .map(memberGroup -> {
                                User member = memberGroup.getUser();
                                if (member == null) {
                                    throw new IllegalStateException("User data is missing for member.");
                                }
                                return new NoticeGroupsResponseDto.Member(member.getId(), member.getName());
                            })
                            .collect(Collectors.toList());

                    // 그룹 카테고리 리스트 생성
                    List<NoticeGroupsResponseDto.CategoryInfo> groupCategory = noticeGroup.getGroupCategory().stream()
                            .map(category -> new NoticeGroupsResponseDto.CategoryInfo(category.getCategoryId(), category.getCategoryName()))
                            .collect(Collectors.toList());

                    // 참가 여부 플래그 설정
                    boolean isParticipant = participantGroupIds.contains(noticeGroup.getGroupId());

                    return new NoticeGroupsResponseDto.Group(
                            noticeGroup.getGroupId(),
                            noticeGroup.getGroupName(),
                            noticeGroup.getAdminId(),
                            noticeGroup.getCrawlSiteUrl(),
                            groupCategory,
                            noticeGroup.getDescription(),
                            noticeGroup.getShareUrl(),
                            members,
                            isParticipant // 참가 여부 플래그 추가
                    );
                })
                .collect(Collectors.toList());
    }



    /* 공지 그룹 제거 */
    @Transactional
    public NoticeGroupDeleteResponseDto deleteNoticeGroup(Long groupId, Authentication authentication) {
        // 1. Authentication 객체에서 사용자 정보 추출
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // 2. NoticeGroup 조회
        NoticeGroup noticeGroup = noticeGroupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("Group not found with ID: " + groupId));

        // 3. 관리자인지 확인
        if (!noticeGroup.getAdminId().equals(user.getId())) {
            throw new IllegalArgumentException("Only the admin can delete this group.");
        }

        // 4. UserGroup 삭제
        userGroupRepository.deleteByNoticeGroup(noticeGroup);

        // 5. NoticeGroup 삭제
        noticeGroupRepository.delete(noticeGroup);

        // 6. 성공 응답 반환
        return new NoticeGroupDeleteResponseDto(
                new NoticeGroupJoinResponseDto.Status("success", "공지 그룹 삭제 완료")
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

        // 5. 공지 저장 로직 처리
        saveRelevantNoticesForUser(user, noticeGroup);

        // 6. 성공 응답 생성
        return new NoticeGroupJoinResponseDto(
                new NoticeGroupJoinResponseDto.Status("success", "Joined group successfully")
        );
    }

    /* 이미 참가한 그룹인지 확인 */
    private boolean isUserAlreadyJoined(User user, NoticeGroup noticeGroup) {
        return userGroupRepository.existsByUserAndNoticeGroup(user, noticeGroup);
    }

    /* 사용자와 공지그룹에 부합하는 공지 저장 */
    private void saveRelevantNoticesForUser(User user, NoticeGroup noticeGroup) {
        // 그룹 카테고리와 사용자 카테고리 가져오기
        Set<Category> groupCategories = noticeGroup.getGroupCategory();
        Set<Category> userCategories = user.getSelectedCategories();

        // 1단계: 공지 카테고리와 공지 그룹 카테고리의 합집합으로 필터링
        List<Notice> noticesMatchingGroup = noticeRepository.findAll().stream()
                .filter(notice -> notice.getCategories().stream().anyMatch(groupCategories::contains))
                .collect(Collectors.toList());

        // 2단계: 위에서 모은 공지들 중 사용자 카테고리와 합집합으로 필터링
        List<Notice> relevantNotices = noticesMatchingGroup.stream()
                .filter(notice -> notice.getCategories().stream().anyMatch(userCategories::contains))
                .collect(Collectors.toList());

        // UserNotice 객체 생성
        List<UserNotice> userNotices = relevantNotices.stream()
                .map(notice -> new UserNotice(user, notice, false, false)) // 기본값으로 isStarred와 isStored는 false
                .collect(Collectors.toList());

        // 중복 방지: 이미 저장된 공지는 제외
        userNotices.forEach(userNotice -> {
            if (!userNoticeRepository.findByUserAndNotice(user, userNotice.getNotice()).isPresent()) {
                userNoticeRepository.save(userNotice);
            }
        });
    }



    //    /* 그룹 참가 로직 구현 */
//    private void joinUserToGroup(User user, NoticeGroup noticeGroup) {
//        try {
//            // UserGroup 생성
//            UserGroup userGroup = new UserGroup(user, noticeGroup, UserRole.Member);
//            // 동기화
//            user.getUserGroups().add(userGroup);
//            noticeGroup.getUserGroups().add(userGroup);
//
//            // 상위 엔티티 저장
//            userRepository.save(user);
//            noticeGroupRepository.save(noticeGroup);
//
//        } catch (IllegalArgumentException e) {
//            // 잘못된 인자 처리
//            throw new RuntimeException("Invalid argument: " + e.getMessage(), e);
//        } catch (DataIntegrityViolationException e) {
//            // 데이터베이스 무결성 관련 예외 처리
//            throw new RuntimeException("Data integrity violation: " + e.getMessage(), e);
//        } catch (Exception e) {
//            // 기타 모든 예외 처리
//            throw new RuntimeException("Unexpected error occurred while joining user to group: " + e.getMessage(), e);
//        }
//    }
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

                // NoticeGroup의 adminId 업데이트
                noticeGroup.setAdminId(newAdmin.getUser().getId());
                noticeGroupRepository.save(noticeGroup);
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
