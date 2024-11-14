package gongalgongal.gongalgongal_spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
        private List<CategoryInfo> categories;

        public Data(String email, String name, List<CategoryInfo> categories) {
            this.email = email;
            this.name = name;
            this.categories = categories;
        }
    }

    @Getter
    @Setter
    public static class CategoryInfo {
        private Long id;
        private String name;

        public CategoryInfo(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
