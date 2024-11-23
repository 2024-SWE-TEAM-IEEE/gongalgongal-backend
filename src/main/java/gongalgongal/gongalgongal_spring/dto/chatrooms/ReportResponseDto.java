package gongalgongal.gongalgongal_spring.dto.chatrooms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDto {
    private Long id; // 신고 ID
    private Long chatId; // 신고된 메시지 ID
    private String content; // 신고된 메시지 내용
    private ReportingUserDto reportingUser; // 신고한 사용자 정보
    private LocalDateTime createdAt; // 신고 일시

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportingUserDto {
        private Long id; // 신고한 사용자 ID
        private String name; // 신고한 사용자 이름
    }
}
