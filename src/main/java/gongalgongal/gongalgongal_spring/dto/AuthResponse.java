package gongalgongal.gongalgongal_spring.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private Status status;
    private Data data;

    public AuthResponse(Status status, Data data) {
        this.status = status;
        this.data = data;
    }

    @Getter
    @Setter
    public static class Status {
        private String type;
        private String message;

        public Status(String type, String message) {
            this.type = type;
            this.message = message;
        }
    }

    // 데이터 세분화
    public interface Data {}

    //로그인, 회원가입 전용 Response
    @Getter
    @Setter
    public static class AccessTokenData implements Data {
        private String accessToken;

        public AccessTokenData(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    // 아이디 중복 여부 확인 전용 Response
    @Getter
    @Setter
    public static class EmailCheckData implements Data {
        private Boolean isDuplicated;

        public EmailCheckData(Boolean isDuplicated) {
            this.isDuplicated = isDuplicated;
        }
    }
}
