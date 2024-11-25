package gongalgongal.gongalgongal_spring.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequest {
    private String email;
    private String name;
    private String password;
}