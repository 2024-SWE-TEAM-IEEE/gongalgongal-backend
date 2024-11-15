package gongalgongal.gongalgongal_spring.controller;

import gongalgongal.gongalgongal_spring.dto.NoticesResponseDto;

import gongalgongal.gongalgongal_spring.service.NoticeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    public ResponseEntity<NoticesResponseDto> getNotices(Authentication authentication) {
        try {
            NoticesResponseDto response = noticeService.getNotices(authentication);
            return ResponseEntity.ok(response); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new NoticesResponseDto(new NoticesResponseDto.Status("failed", e.getMessage()), null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new NoticesResponseDto(new NoticesResponseDto.Status("failed", "Internal server error"), null));
        }
    }
}
