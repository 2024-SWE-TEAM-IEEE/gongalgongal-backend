package gongalgongal.gongalgongal_spring.controller;

import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateRequestDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupCreateResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupJoinResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupsResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeGroupLeaveResponseDto;

import gongalgongal.gongalgongal_spring.service.NoticeGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

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
            @RequestBody NoticeGroupCreateRequestDto request,
            Authentication authentication) { // Authentication 객체 추가

        if (request.getGroupName() == null || request.getDescription() == null) {
            // 필수 파라미터 누락 시 실패 응답 생성
            NoticeGroupCreateResponseDto response = new NoticeGroupCreateResponseDto(
                    new NoticeGroupCreateResponseDto.Status("failed", "Missing required parameters"), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            // 서비스 계층에서 공지 그룹 생성 처리
            NoticeGroupCreateResponseDto response = noticeGroupService.createNoticeGroup(request, authentication);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // 잘못된 입력값 예외 처리
            NoticeGroupCreateResponseDto response = new NoticeGroupCreateResponseDto(
                    new NoticeGroupCreateResponseDto.Status("failed", e.getMessage()), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            // 예외 상황에 대한 응답 설정
            NoticeGroupCreateResponseDto response = new NoticeGroupCreateResponseDto(
                    new NoticeGroupCreateResponseDto.Status("failed", "Internal server error occurred"), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 참가한 공지 그룹 리스트 조회
    @GetMapping
    public ResponseEntity<NoticeGroupsResponseDto> getJoinedNoticeGroups(Authentication authentication) {
        try {
            // Authentication 객체를 서비스로 전달
            NoticeGroupsResponseDto response = noticeGroupService.getJoinedNoticeGroups(authentication);
            return ResponseEntity.ok(response); // 200 OK
        } catch (Exception e) {
            // 에러 응답
            NoticeGroupsResponseDto response = new NoticeGroupsResponseDto(
                    new NoticeGroupsResponseDto.Status("failed", "Internal server error"),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/{group_id}/join")
    public ResponseEntity<NoticeGroupJoinResponseDto> joinNoticeGroup(
            @PathVariable("group_id") Long groupId,
            Authentication authentication) { // 인증 정보 추가
        try {
            NoticeGroupJoinResponseDto response = noticeGroupService.joinNoticeGroup(groupId, authentication);
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

    @PostMapping("/{group_id}/leave")
    public ResponseEntity<NoticeGroupLeaveResponseDto> leaveNoticeGroup(
            @PathVariable("group_id") Long groupId,
            Authentication authentication) {
        try {
            NoticeGroupLeaveResponseDto response = noticeGroupService.leaveNoticeGroup(groupId, authentication);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new NoticeGroupLeaveResponseDto(
                            new NoticeGroupLeaveResponseDto.Status("failed", e.getMessage())
                    ));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new NoticeGroupLeaveResponseDto(
                            new NoticeGroupLeaveResponseDto.Status("failed", e.getMessage())
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new NoticeGroupLeaveResponseDto(
                            new NoticeGroupLeaveResponseDto.Status("failed", "Internal server error occurred")
                    ));
        }
    }

}
