package gongalgongal.gongalgongal_spring.controller;

import gongalgongal.gongalgongal_spring.dto.chatrooms.ReportRequestDto;
import gongalgongal.gongalgongal_spring.dto.chatrooms.ReportResponseDto;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.repository.UserRepository;
import gongalgongal.gongalgongal_spring.service.ReportService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms/{notice_id}/chats/{chat_id}/report")
public class ReportController {
    private final ReportService reportService;
    private final UserRepository userRepository; // UserRepository를 생성자 주입으로 선언

    @PostMapping
    public ResponseEntity<?> reportChatMessage(
            @PathVariable("chat_id") Long chatId,
            @RequestBody ReportRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails // 인증된 사용자 정보
    ) {
        // JWT에서 이메일 추출
        String email = userDetails.getUsername();

        // 이메일로 사용자 조회
        User reportingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        // 사용자 ID 가져오기
        Long reportingUserId = reportingUser.getId();

        try {
            // 신고 생성
            ReportResponseDto responseDto = reportService.createReport(chatId, reportingUserId, requestDto);

            // 성공 응답 반환
            return ResponseEntity.ok(new ResponseWrapper(
                    "success",
                    "해당 메시지가 신고되었습니다.",
                    responseDto
            ));
        } catch (IllegalArgumentException e) {
            // 실패 응답 반환
            return ResponseEntity.badRequest().body(new ResponseWrapper(
                    "failed",
                    e.getMessage(),
                    null
            ));
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class ResponseWrapper {
        private String type;
        private String message;
        private ReportResponseDto data;
    }
}
