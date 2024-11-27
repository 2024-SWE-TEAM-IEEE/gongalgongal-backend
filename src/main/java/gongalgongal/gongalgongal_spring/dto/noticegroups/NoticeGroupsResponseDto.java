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
public class NoticeGroupsResponseDto extends ResponseTemplate<NoticeGroupsResponseDto.GroupListData> {

    public NoticeGroupsResponseDto(Status status, GroupListData data) {
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
        private Long id;
        private String name;
        private Long adminId;
        private String siteUrl;
        private List<CategoryInfo> groupCategory;
        private String description;
        private String shareUrl;
        private List<Member> members;
    }

    @Data
    @AllArgsConstructor
    public static class Member {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class CategoryInfo {
        private Long id;
        private String name;
    }
}
