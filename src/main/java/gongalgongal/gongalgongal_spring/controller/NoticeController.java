package gongalgongal.gongalgongal_spring.controller;

import gongalgongal.gongalgongal_spring.dto.NoticesResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticesDetailResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeStoreResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeStoreDeleteResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeStarResponseDto;
import gongalgongal.gongalgongal_spring.dto.NoticeStarDeleteResponseDto;

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
    public ResponseEntity<NoticeStoreDeleteResponseDto> unstoreNotice(
            @PathVariable("notice_id") Long noticeId,
            Authentication authentication) {
        try {
            noticeService.unstoreNotice(noticeId, authentication);
            NoticeStoreDeleteResponseDto.Status status = new NoticeStoreDeleteResponseDto.Status("success", "Notice successfully unstored");
            return ResponseEntity.status(HttpStatus.OK).body(new NoticeStoreDeleteResponseDto(status));
        } catch (IllegalArgumentException e) {
            NoticeStoreDeleteResponseDto.Status status = new NoticeStoreDeleteResponseDto.Status("failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NoticeStoreDeleteResponseDto(status));
        } catch (Exception e) {
            NoticeStoreDeleteResponseDto.Status status = new NoticeStoreDeleteResponseDto.Status("failed", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NoticeStoreDeleteResponseDto(status));
        }
    }

    @PostMapping("/{notice_id}/star")
    public ResponseEntity<NoticeStarResponseDto> starNotice(
            @PathVariable("notice_id") Long noticeId,
            Authentication authentication) {
        try {
            NoticeStarResponseDto response = noticeService.starNotice(noticeId, authentication);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            NoticeStarResponseDto.Status status = new NoticeStarResponseDto.Status("failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NoticeStarResponseDto(status));
        } catch (Exception e) {
            NoticeStarResponseDto.Status status = new NoticeStarResponseDto.Status("failed", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NoticeStarResponseDto(status));
        }
    }

    @DeleteMapping("/{notice_id}/star")
    public ResponseEntity<NoticeStarDeleteResponseDto> unstarNotice(
            @PathVariable("notice_id") Long noticeId,
            Authentication authentication) {
        try {
            noticeService.unstarNotice(noticeId, authentication);
            NoticeStarDeleteResponseDto.Status status = new NoticeStarDeleteResponseDto.Status("success", "Notice successfully unstarred");
            return ResponseEntity.status(HttpStatus.OK).body(new NoticeStarDeleteResponseDto(status));
        } catch (IllegalArgumentException e) {
            NoticeStarDeleteResponseDto.Status status = new NoticeStarDeleteResponseDto.Status("failed", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NoticeStarDeleteResponseDto(status));
        } catch (Exception e) {
            NoticeStarDeleteResponseDto.Status status = new NoticeStarDeleteResponseDto.Status("failed", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new NoticeStarDeleteResponseDto(status));
        }
    }
}
