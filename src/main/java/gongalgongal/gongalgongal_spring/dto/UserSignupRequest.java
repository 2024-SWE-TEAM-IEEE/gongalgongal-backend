package gongalgongal.gongalgongal_spring.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequest {
    private String email;
    private String name;
    private String password;
    private List<String> preferredTags;
}