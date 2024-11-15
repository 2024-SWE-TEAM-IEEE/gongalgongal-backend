/*
API: 공지 그룹 탈퇴
Response:
    {
      "status" : {
        "type": "success" | "failed"
        "message": : "~~"
      },
      "data" : { }
    }
*/


package gongalgongal.gongalgongal_spring.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeGroupLeaveResponseDto extends ResponseTemplate<Object> {

    public NoticeGroupLeaveResponseDto(Status status) {
        super(status, new Object()); // 빈 data 객체를 반환
    }
}
