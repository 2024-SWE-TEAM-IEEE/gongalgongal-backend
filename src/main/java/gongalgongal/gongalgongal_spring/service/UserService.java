package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.UpdateUserRequest;
import gongalgongal.gongalgongal_spring.dto.UserInfoResponse;
import gongalgongal.gongalgongal_spring.model.Category;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.repository.CategoryRepository;
import gongalgongal.gongalgongal_spring.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public UserService(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
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

    public Optional<User> updateUser(String email, UpdateUserRequest request) {
        return userRepository.findByEmail(email).map(user -> {
            user.setName(request.getName());

            // 선택된 카테고리 ID로 카테고리 목록 조회
            Set<Category> categories = request.getSelectedCategoryIds().stream()
                    .map(categoryRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());

            user.setSelectedCategories(categories);
            return userRepository.save(user);
        });
    }
}
