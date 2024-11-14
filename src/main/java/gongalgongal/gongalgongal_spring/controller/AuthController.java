package gongalgongal.gongalgongal_spring.controller;

import gongalgongal.gongalgongal_spring.dto.AuthResponse;
import gongalgongal.gongalgongal_spring.dto.UserLoginRequest;
import gongalgongal.gongalgongal_spring.dto.UserSignupRequest;
import gongalgongal.gongalgongal_spring.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 회원가입 엔드포인트
    @PostMapping("/join")
    public ResponseEntity<AuthResponse> register(@RequestBody UserSignupRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // 로그인 엔드포인트
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

