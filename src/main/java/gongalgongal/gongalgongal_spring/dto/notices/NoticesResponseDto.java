package gongalgongal.gongalgongal_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticesResponseDto extends ResponseTemplate<NoticesResponseDto.NoticeListData> {

    public NoticesResponseDto(Status status, NoticeListData data) {
        super(status, data);
    }

    @Data
    @AllArgsConstructor
    public static class NoticeListData {
        private List<Notice> notices;
    }

    @Data
    @AllArgsConstructor
    public static class Notice {
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
