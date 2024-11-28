package gongalgongal.gongalgongal_spring.controller;

import gongalgongal.gongalgongal_spring.dto.chatrooms.ChatMessageRequestDto;
import gongalgongal.gongalgongal_spring.dto.chatrooms.ChatMessageResponseDto;
import gongalgongal.gongalgongal_spring.service.ChatMessageService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms/{notice_id}/chat")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @PostMapping
    public ResponseEntity<?> createChatMessage(
            @PathVariable("notice_id") Long noticeId,
            @RequestBody ChatMessageRequestDto requestDto
    ) {
        // JWT에서 이메일 추출
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 서비스 호출
        ChatMessageResponseDto responseDto = chatMessageService.createChatMessage(noticeId, email, requestDto);

        // 응답 반환
        return ResponseEntity.ok().body(new ResponseWrapper(
                "success",
                "Message created successfully",
                responseDto
        ));
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class ResponseWrapper {
        private String type;
        private String message;
        private ChatMessageResponseDto data;
    }
}
