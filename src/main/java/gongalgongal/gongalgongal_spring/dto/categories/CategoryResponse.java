package gongalgongal.gongalgongal_spring.dto.categories;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryResponse {
    private Status status;
    private Data data;

    public CategoryResponse(Status status, Data data) {
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
        private List<CategoryInfo> categories;

        public Data(List<CategoryInfo> categories) {
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