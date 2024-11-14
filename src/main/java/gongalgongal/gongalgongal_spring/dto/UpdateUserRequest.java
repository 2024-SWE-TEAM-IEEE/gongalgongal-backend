package gongalgongal.gongalgongal_spring.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateUserRequest {
    private String name;
    private String email;
    private String password;
    private List<Long> selectedCategoryIds;
}
