package sms.uccbackend.domain.auth.authController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sms.uccbackend.domain.auth.authDto.LoginRequest;
import sms.uccbackend.domain.auth.authDto.LoginResponse;
import sms.uccbackend.domain.auth.authDto.SignupRequest;
import sms.uccbackend.domain.auth.authService.AuthService;
import sms.uccbackend.domain.user.userDto.UserResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // POST /auth/login - Firebase 토큰으로 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // POST /auth/signup - 최초 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
//            @RequestHeader("X-User-Id") Long userId,
//            @RequestBody SignupRequest request
            @RequestHeader("Authorization") String firebaseIdToken,
            @RequestBody SignupRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.signup(firebaseIdToken, request));
    }

    // GET /auth/me - 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(authService.getMe(userId));
    }
}
