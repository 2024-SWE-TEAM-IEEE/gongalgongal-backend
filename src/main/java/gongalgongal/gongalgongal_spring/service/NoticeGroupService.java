package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateRequestDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupJoinResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupsResponseDto;

import gongalgongal.gongalgongal_spring.model.NoticeGroup;
import gongalgongal.gongalgongal_spring.repository.NoticeGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.List;

@Service
public class NoticeGroupService {

    private final NoticeGroupRepository noticeGroupRepository;

    @Autowired
    public NoticeGroupService(NoticeGroupRepository noticeGroupRepository) {
        this.noticeGroupRepository = noticeGroupRepository;
    }

    /* 공지 그룹 생성 */
    public NoticeGroupCreateResponseDto createNoticeGroup(NoticeGroupCreateRequestDto request) {

//        // 객체 생성
//        savedNoticeGroup = saveNoticeGroup(request, shareUrl);
//
//        // 공유 URL 생성 후 url 업데이트
//        String shareUrl = makeShareURL(savedNoticeGroup.getNoticeId());
//        savedNoticeGroup.setShareUrl(shareUrl);
//        noticeGroupRepository.save(savedNoticeGroup);

        // 응답 DTO 생성
        NoticeGroupCreateResponseDto.Status status = new NoticeGroupCreateResponseDto.Status("success", "Group created successfully");
//        NoticeGroupCreateResponseDto.GroupCreationData data = new NoticeGroupCreateResponseDto.GroupCreationData(true, savedNotice.getNoticeId().toString(), shareUrl);
        NoticeGroupCreateResponseDto.GroupCreationData data = new NoticeGroupCreateResponseDto.GroupCreationData(true, (long) 1, "https://www.dongguk.edu/main/share"); // 그룹 ID를 임의 설정

        return new NoticeGroupCreateResponseDto(status, data);
    }

    // [[TODO]]
    private String makeShareURL() {
        return "";
    }

    // [[TODO]]
    private void saveNoticeGroup(NoticeGroupCreateRequestDto request) {
//        NoticeGroup noticeGroup = new NoticeGroup(
//                request.getAdminId(),
//                request.getGroupName(),
//                null,
//                request.getCrawlSiteUrl()
//        );
//        NoticeGroup savedNoticeGroup = noticeGroupRepository.save(noticeGroup);
//        return savedNoticeGroup
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
