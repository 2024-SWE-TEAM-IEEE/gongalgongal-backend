package gongalgongal.gongalgongal_spring.controller;

import gongalgongal.gongalgongal_spring.dto.ChatroomJoinResponseDto;

import gongalgongal.gongalgongal_spring.service.ChatroomService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;

    @PostMapping("/{notice_id}/join")
    public ResponseEntity<ChatroomJoinResponseDto> joinChatroom(
            @PathVariable("notice_id") Long noticeId,
            Authentication authentication) {
        try {
            // 서비스 호출
            ChatroomJoinResponseDto response = chatroomService.joinChatroom(noticeId, authentication);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // 사용자 입력 관련 예외 처리
            ChatroomJoinResponseDto.Status status = new ChatroomJoinResponseDto.Status("failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ChatroomJoinResponseDto(status, null));
        } catch (RuntimeException e) {
            // 런타임 예외 처리
            ChatroomJoinResponseDto.Status status = new ChatroomJoinResponseDto.Status("failed", "A runtime error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ChatroomJoinResponseDto(status, null));
        } catch (Exception e) {
            // 기타 예외 처리
            ChatroomJoinResponseDto.Status status = new ChatroomJoinResponseDto.Status("failed", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ChatroomJoinResponseDto(status, null));
        }
    }
}
