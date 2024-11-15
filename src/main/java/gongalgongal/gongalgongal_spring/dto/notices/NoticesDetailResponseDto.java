/*
API: 공지 상세 조회
Response:
    {
      "status" : {
        "type": "success" | "failed"
        "message": : "~~"
      },
      "data" : {
        "notices": {
              "id": 1,
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
              "content": "2024년도 장학금 신청을 받습니다. 신청 방법은...",
              "author": "학생처",
              "noticed_date": "2024-11-01T10:00:00",
              "url": "https://school.edu/notice/12345",
              "is_favorited": true, //알림 보관 표시
              "is_starred": false, //알림 중요 표시
              "is_participating": false //공지 대화방 이미 참여 중인지 아닌지
            }
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
public class NoticesDetailResponseDto extends ResponseTemplate<NoticesDetailResponseDto.NoticeDetail> {

    public NoticesDetailResponseDto(Status status, NoticeDetail data) {
        super(status, data);
    }

    @Data
    @AllArgsConstructor
    public static class NoticeDetail {
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
