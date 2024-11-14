package gongalgongal.gongalgongal_spring.controller;

import gongalgongal.gongalgongal_spring.dto.UpdateUserRequest;
import gongalgongal.gongalgongal_spring.dto.UserInfoResponse;
import gongalgongal.gongalgongal_spring.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(Authentication authentication) {
        String email = authentication.getName();

        return userService.getUserInfo(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new UserInfoResponse(
                                new UserInfoResponse.Status("failed", "User not found"),
                                null
                        )));
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Map<String, ?>>> updateUserInfo(
            Authentication authentication,
            @RequestBody UpdateUserRequest request) {

        String email = authentication.getName();

        return userService.updateUser(email, request)
                .map(user -> ResponseEntity.ok(Map.of(
                        "status", Map.of("type", "success", "message", "User information updated successfully"),
                        "data", Map.of(
                                "email", user.getEmail(),
                                "name", user.getName(),
                                "categories", user.getSelectedCategories().stream()
                                        .map(category -> Map.of("id", category.getCategoryId(), "name", category.getCategoryName()))
                                        .collect(Collectors.toList())
                        )
                )))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of(
                        "status", Map.of("type", "failed", "message", "User not found")
                )));
    }
}