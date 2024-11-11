/*
    {
      "status" : {
        "type": "success" | "failed"
        "message": : "~~"
      },
      "data" : { }
    }

위와 같은 response 양식을 맞추기 위한 가상 클래스 ResponseTemplate 정의
*/

package gongalgongal.gongalgongal_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // 모든 필드를 받는 생성자 자동 생성
public abstract class ResponseTemplate<T> {

    private Status status;
    private T data;

    // Status 내부 클래스 정의
    @Data
    @AllArgsConstructor
    public static class Status {
        private String type;       // "success" or "failed"
        private String message;
    }
}
