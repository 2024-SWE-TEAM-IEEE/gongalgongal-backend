/*
API: 공지 알림 리스트 조회
Response:
    {
      "status" : {
        "type": "success" | "failed"
        "message": : "~~"
      },
      "data" : {
        "notices": [
              {
                "id": 1, // id는 앞에 뭐 안붙이는걸로 통일
                "title": "장학금 신청 안내",
              "categories": [
                {
                   "id": "1",
                   "name": "장학금"
                 },
                 {
                   "id": "3",
                   "name": "대외활동"
                 }
              ],
              "author": "학생처", // poster, author 중 하나만 일괄적으로 선택하기. 이전 파일에는 author라고 작성함
              "noticed_date": "2024-10-15T15:00:00", // 이름 변경
              "is_favorited": true,
              "is_starred": false
            }
          ]
      }
    }
*/

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
        private List<Category> categories;
        private String content;
        private String author;
        private LocalDateTime noticedDate;
        private String url;
        private boolean isStored;
        private boolean isStarred;
        private boolean isParticipating;
    }

    @Data
    @AllArgsConstructor
    public static class Category {
        private long id;
        private String name;
    }
}
