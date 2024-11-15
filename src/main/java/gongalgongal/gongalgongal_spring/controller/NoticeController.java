package gongalgongal.gongalgongal_spring.controller;

import gongalgongal.gongalgongal_spring.dto.NoticesResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticesDetailResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeStoreResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeDeleteResponseDto;

import gongalgongal.gongalgongal_spring.service.NoticeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @GetMapping("/{notice_id}")
    public ResponseEntity<NoticesDetailResponseDto> getNoticeDetail(
            @PathVariable("notice_id") Long noticeId,
            Authentication authentication) {
        try {
            NoticesDetailResponseDto response = noticeService.getNoticeDetail(noticeId, authentication);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 400 Bad Request 처리
            NoticesDetailResponseDto.Status status = new NoticesDetailResponseDto.Status(
                    "failed",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NoticesDetailResponseDto(status, null));
        } catch (RuntimeException e) {
            // 404 Not Found 처리
            NoticesDetailResponseDto.Status status = new NoticesDetailResponseDto.Status(
                    "failed",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NoticesDetailResponseDto(status, null));
        }
    }

    @PostMapping("/{notice_id}/store")
    public ResponseEntity<NoticeStoreResponseDto> storeNotice(
            @PathVariable("notice_id") Long noticeId,
            Authentication authentication) {
        try {
            NoticeStoreResponseDto response = noticeService.storeNotice(noticeId, authentication);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            NoticeStoreResponseDto.Status status = new NoticeStoreResponseDto.Status("failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NoticeStoreResponseDto(status));
        } catch (Exception e) {
            NoticeStoreResponseDto.Status status = new NoticeStoreResponseDto.Status("failed", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NoticeStoreResponseDto(status));
        }
    }

    @DeleteMapping("/{notice_id}/store")
    public ResponseEntity<NoticeDeleteResponseDto> unstoreNotice(
            @PathVariable("notice_id") Long noticeId,
            Authentication authentication) {
        try {
            noticeService.unstoreNotice(noticeId, authentication);
            NoticeDeleteResponseDto.Status status = new NoticeDeleteResponseDto.Status("success", "Notice successfully unstored");
            return ResponseEntity.status(HttpStatus.OK).body(new NoticeDeleteResponseDto(status));
        } catch (IllegalArgumentException e) {
            NoticeDeleteResponseDto.Status status = new NoticeDeleteResponseDto.Status("failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NoticeDeleteResponseDto(status));
        } catch (Exception e) {
            NoticeDeleteResponseDto.Status status = new NoticeDeleteResponseDto.Status("failed", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NoticeDeleteResponseDto(status));
        }
    }
}
