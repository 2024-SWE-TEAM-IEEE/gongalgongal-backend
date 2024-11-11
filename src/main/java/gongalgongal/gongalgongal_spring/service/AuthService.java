package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.model.User;
import gongalgongal.gongalgongal_spring.repository.UserRepository;
import gongalgongal.gongalgongal_spring.response.AuthResponse;
import gongalgongal.gongalgongal_spring.response.UserLoginRequest;
import gongalgongal.gongalgongal_spring.response.UserSignupRequest;
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
    public String register(UserSignupRequest request) {
        // 이미 존재하는 이메일인지 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        // 비밀번호 암호화 후 유저 생성
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPreferredTags(request.getPreferredTags());
        userRepository.save(user);

        return "User registered successfully";
    }

    // 로그인 메서드
    public AuthResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials.");
        }

        // JWT 토큰 생성
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}

