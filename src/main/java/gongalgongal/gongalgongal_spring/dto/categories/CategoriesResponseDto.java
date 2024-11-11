/*
API: 카테고리 리스트 전달
Response:
    {
      "status" : {
        "type": "success" | "failed"
        "message": : "~~"
      },
      "data" : {
         "categories": [
            {
                "id": "1",
                "name": "장학"
            },
            {
                "id": "2",
                "name": "교외활동"
            },
            {
                "id": "3",
                "name": "특강"
            },
            ..
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
public class CategoriesResponseDto extends ResponseTemplate<CategoriesResponseDto.CategoryData> {

    public CategoriesResponseDto(Status status, CategoryData data) {
        super(status, data);
    }

    @Data
    @AllArgsConstructor
    public static class CategoryData {
        private List<Category> categories;
    }

    @Data
    @AllArgsConstructor
    public static class Category {
        private String id;
        private String name;
    }
}
