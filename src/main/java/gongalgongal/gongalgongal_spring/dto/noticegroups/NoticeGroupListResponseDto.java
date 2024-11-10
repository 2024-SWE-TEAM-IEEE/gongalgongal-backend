/*
API: 참가한 공지 그룹 리스트 조회
Response:
    {
      "status" : {
        "type": "success" | "failed"
        "message": : "~~"
      },
      "data" : {
         "groups": [
          {
             "id": "group123",
             "name": "기술 공지 그룹",
             "admin_id": 1, // 그룹장 id
             "site_url": "https://www.dongguk.edu/article/JANGHAKNOTICE/list"
             "group_category": [
                 1,
                 2
             ]
             "description": "기술 관련 공지 사항을 공유하는 그룹입니다."
             "share_url": "https://example.com/invite/group123",
             "members": [
                    {
                      "id": 1,
                      "name": "사용자A"
                    },
                    {
                      "id": 2,
                      "name": "사용자B"
                    },
                    {
                      "id": 3,
                      "name": "사용자C"
                    }
                  ]
          },
          {
             "id": "group456",
             "name": "예술 공지 그룹",
             "admin_id": 2, // 그룹장 id
             "site_url": "https://www.dongguk.edu/article/JANGHAKNOTICE/list"
             "group_category": [
                 3,
                 4
             ]
             "description": "예술 관련 공지 사항을 공유하는 그룹입니다."
             "share_url": "https://example.com/invite/group123",
             "members": [
                    {
                      "id": 1,
                      "name": "사용자A"
                    },
                    {
                      "id": 2,
                      "name": "사용자B"
                    },
                    {
                      "id": 3,
                      "name": "사용자C"
                    }
                  ]
          }
        ]
      }
    }
*/

package gongalgongal.gongalgongal_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeGroupListResponseDto extends ResponseTemplate<NoticeGroupListResponseDto.GroupListData> {

    public NoticeGroupListResponseDto(Status status, GroupListData data) {
        super(status, data);
    }

    @Data
    @AllArgsConstructor
    public static class GroupListData {
        private List<Group> groups;
    }

    @Data
    @AllArgsConstructor
    public static class Group {
        private String id;
        private String name;
        private int admin_id;
        private String site_url;
        private List<Integer> group_category;
        private String description;
        private String share_url;
        private List<Member> members;
    }

    @Data
    @AllArgsConstructor
    public static class Member {
        private int id;
        private String name;
    }
}
