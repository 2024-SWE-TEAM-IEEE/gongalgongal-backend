package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.UserInfoResponse;
import gongalgongal.gongalgongal_spring.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserInfoResponse> getUserInfo(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserInfoResponse(
                        new UserInfoResponse.Status("success", "User information retrieved successfully"),
                        new UserInfoResponse.Data(
                                user.getEmail(),
                                user.getName(),
                                user.getSelectedCategories().stream()
                                        .map(category -> new UserInfoResponse.CategoryInfo(
                                                category.getCategoryId(),
                                                category.getCategoryName()
                                        ))
                                        .collect(Collectors.toList())
                        )
                ));
    }
}
