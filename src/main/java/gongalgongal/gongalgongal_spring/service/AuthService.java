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
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

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
        AuthResponse.AccessTokenData data = new AuthResponse.AccessTokenData(token);
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
            AuthResponse.Status status = new AuthResponse.Status("failed", "Incorrect password");
            return new AuthResponse(status, null);
        }

        // 로그인 성공 시 JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getEmail());

        // 성공 응답 생성 및 반환
        AuthResponse.Status status = new AuthResponse.Status("success", "Login successful");
        AuthResponse.AccessTokenData data = new AuthResponse.AccessTokenData(token);
        return new AuthResponse(status, data);
    }

    // 이메일 검증 메서드
    public AuthResponse checkEmail(String email) {
        boolean isDuplicated = userRepository.existsByEmail(email);
        String message = isDuplicated ? "Email already exists" : "Email is available";

        AuthResponse.Status status = new AuthResponse.Status(
                isDuplicated ? "failed" : "success",
                message
        );

        AuthResponse.EmailCheckData data = new AuthResponse.EmailCheckData(isDuplicated);

        return new AuthResponse(status, data);
    }

    //비밀번호 찾기 메소드
    public void forgotPassword(String email) {
        // 1. 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("The provided email address does not exist in our records"));

        // 2. 임시 비밀번호 생성
        String temporaryPassword = UUID.randomUUID().toString().substring(0, 8); // 8자리 랜덤 문자열

        // 3. 임시 비밀번호 암호화 및 저장
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);

        // 4. 이메일 전송
        emailService.sendEmail(
                email,
                "공알공알 임시 비밀번호",
                user.getName() + "님의 임시 비밀번호는 " + temporaryPassword + "입니다. 로그인 후 비밀번호를 변경해주세요."
        );
    }
}

