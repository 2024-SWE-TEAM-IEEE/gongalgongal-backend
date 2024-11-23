package gongalgongal.gongalgongal_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticesDetailResponseDto extends ResponseTemplate<NoticesDetailResponseDto.NoticeDetail> {

    public NoticesDetailResponseDto(Status status, NoticeDetail data) {
        super(status, data);
    }

    @Data
    @AllArgsConstructor
    public static class NoticeDetail {
        private long id;
        private String title;
        private String content;
        private String author;
        private LocalDateTime noticedDate;
        private String url;
        private boolean isStored;
        private boolean isStarred;
        private boolean isParticipating;
    }

}
