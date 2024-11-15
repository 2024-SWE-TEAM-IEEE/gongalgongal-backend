package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.UpdateUserRequest;
import gongalgongal.gongalgongal_spring.dto.UserInfoResponse;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserInfoResponse> getUserInfo(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserInfoResponse(
                        new UserInfoResponse.Status("success", "User information retrieved successfully"),
                        new UserInfoResponse.Data(
                                user.getEmail(),
                                user.getName()
                        )
                ));
    }

    public Optional<User> updateUser(String email, UpdateUserRequest request) {
        return userRepository.findByEmail(email).map(user -> {
            user.setName(request.getName());

            // 비밀번호 업데이트 (암호화 후 저장)
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }


            user.setSelectedCategories(categories);
            return userRepository.save(user);
        });
    }
}
