package gongalgongal.gongalgongal_spring.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class UserInfoResponse {

    private Status status;
    private Data data;

    public UserInfoResponse(Status status, Data data) {
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

    @Getter
    @Setter
    public static class Data {
        private String email;
        private String name;
    }

}
