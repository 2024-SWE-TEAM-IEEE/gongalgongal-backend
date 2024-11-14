package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.model.Category;
import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.repository.CategoryRepository;
import gongalgongal.gongalgongal_spring.repository.UserRepository;
import gongalgongal.gongalgongal_spring.dto.AuthResponse;
import gongalgongal.gongalgongal_spring.dto.UserLoginRequest;
import gongalgongal.gongalgongal_spring.dto.UserSignupRequest;
import gongalgongal.gongalgongal_spring.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 회원가입 메서드
    public AuthResponse register(UserSignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            AuthResponse.Status status = new AuthResponse.Status("failed", "Email already in use.");
            return new AuthResponse(status, null);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // selectedCategoryIds가 null인 경우 빈 리스트로 초기화
        List<Long> selectedCategoryIds = request.getSelectedCategoryIds() != null ? request.getSelectedCategoryIds() : new ArrayList<>();

        // 카테고리 ID에 해당하는 카테고리 엔티티 조회 및 설정
        List<Category> selectedCategories = categoryRepository.findAllById(selectedCategoryIds);
        user.setSelectedCategories(new HashSet<>(selectedCategories));

        System.out.println("Selected Category IDs: " + selectedCategoryIds);
        System.out.println("Selected Categories: " + selectedCategories);

        userRepository.save(user);

        // 로그인 성공 시 JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getEmail());

        AuthResponse.Status status = new AuthResponse.Status("success", "User registered successfully");
        AuthResponse.Data data = new AuthResponse.Data(token);
        return new AuthResponse(status, data);
    }

    // 로그인 메서드
    public AuthResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null); // 유저가 없으면 null 반환

        if (user == null) {
            // 사용자 없음 -> 실패 응답 생성
            AuthResponse.Status status = new AuthResponse.Status("failed", "User not found");
            return new AuthResponse(status, null);
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 비밀번호 불일치 -> 실패 응답 생성
            AuthResponse.Status status = new AuthResponse.Status("failed", "Invalid credentials");
            return new AuthResponse(status, null);
        }

        // 로그인 성공 시 JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getEmail());

        // 성공 응답 생성 및 반환
        AuthResponse.Status status = new AuthResponse.Status("success", "Login successful");
        AuthResponse.Data data = new AuthResponse.Data(token);
        return new AuthResponse(status, data);
    }
}

