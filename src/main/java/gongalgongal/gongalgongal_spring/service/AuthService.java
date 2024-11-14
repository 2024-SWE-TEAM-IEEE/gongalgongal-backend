package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.repository.UserRepository;
import gongalgongal.gongalgongal_spring.dto.AuthResponse;
import gongalgongal.gongalgongal_spring.dto.UserLoginRequest;
import gongalgongal.gongalgongal_spring.dto.UserSignupRequest;
import gongalgongal.gongalgongal_spring.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 회원가입 메서드
    public AuthResponse register(UserSignupRequest request) {
        // 이미 존재하는 이메일인지 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            AuthResponse.Status status = new AuthResponse.Status("failed", "Email already in use.");
            return new AuthResponse(status, null);
        }

        // 비밀번호 암호화 후 유저 생성
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPreferredTags(request.getPreferredTags());
        userRepository.save(user);

        // 회원가입 성공 응답 반환
        AuthResponse.Status status = new AuthResponse.Status("success", "User registered successfully");
        return new AuthResponse(status, null);
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

