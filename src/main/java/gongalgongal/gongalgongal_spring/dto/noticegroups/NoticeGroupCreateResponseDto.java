/*
API: 공지 그룹 생성
Response:
    {
      "status" : {
        "type": "success" | "failed"
        "message": : "~~"
      },
      "data" : {
        "created": boolean,
        "group_id": "group123",
        "share_url": "https://example.com/invite/group123"
    }
*/

package gongalgongal.gongalgongal_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeGroupCreateResponseDto extends ResponseTemplate<NoticeGroupCreateResponseDto.GroupCreationData> {

    public NoticeGroupCreateResponseDto(Status status, GroupCreationData data) {
        super(status, data);
    }

    @Data
    @AllArgsConstructor
    public static class GroupCreationData {
        private boolean created;
        private Long groupId;
        private String shareUrl;
    }
}