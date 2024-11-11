package gongalgongal.gongalgongal_spring.controller;

import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateRequestDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupJoinResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupsResponseDto;

import gongalgongal.gongalgongal_spring.service.NoticeGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/notice-groups")
public class NoticeGroupController {

    private final NoticeGroupService noticeGroupService;

    @Autowired
    public NoticeGroupController(NoticeGroupService noticeGroupService) {
        this.noticeGroupService = noticeGroupService;
    }

    // 공지 그룹 생성
    @PostMapping
    public ResponseEntity<NoticeGroupCreateResponseDto> createNoticeGroup(
            @RequestBody NoticeGroupCreateRequestDto request) {

        if (request.getGroupName() == null || request.getDescription() == null) {
            // 필수 파라미터 누락 시 실패 응답 생성
            NoticeGroupCreateResponseDto response = new NoticeGroupCreateResponseDto(
                    new NoticeGroupCreateResponseDto.Status("failed", "Missing required parameters"), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            // 서비스 계층에서 공지 그룹 생성 처리
            NoticeGroupCreateResponseDto response = noticeGroupService.createNoticeGroup(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // 예외 상황에 대한 응답 설정
            NoticeGroupCreateResponseDto response = new NoticeGroupCreateResponseDto(
                    new NoticeGroupCreateResponseDto.Status("failed", "Internal server error occurred"), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 참가한 공지 그룹 리스트 조회
    @GetMapping
    public ResponseEntity<NoticeGroupsResponseDto> getJoinedNoticeGroups() { // [[TODO]] 유저 토큰을 파라미터로 받아야함
        try {
            long userId = (long) 1;
            NoticeGroupsResponseDto response = noticeGroupService.getJoinedNoticeGroups(userId);
            return ResponseEntity.ok(response); // 200 OK
        } catch (Exception e) {
            NoticeGroupsResponseDto response = new NoticeGroupsResponseDto(
                    new NoticeGroupsResponseDto.Status("failed", "Internal server error"),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/{group_id}/join")
    public ResponseEntity<NoticeGroupJoinResponseDto> joinNoticeGroup(
            @PathVariable("group_id") Long groupId) {  // URL에서 group_id를 받아옴

        // group_id가 제대로 전달되는지 확인하기 위해 출력
        System.out.println("Received group_id: " + groupId);

        try {
            NoticeGroupJoinResponseDto response = noticeGroupService.joinNoticeGroup(groupId);
            return ResponseEntity.ok(response); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new NoticeGroupJoinResponseDto(new NoticeGroupJoinResponseDto.Status("failed", e.getMessage())));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new NoticeGroupJoinResponseDto(new NoticeGroupJoinResponseDto.Status("failed", e.getMessage())));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new NoticeGroupJoinResponseDto(new NoticeGroupJoinResponseDto.Status("failed", e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new NoticeGroupJoinResponseDto(new NoticeGroupJoinResponseDto.Status("failed", "Internal server error")));
        }
    }



}
