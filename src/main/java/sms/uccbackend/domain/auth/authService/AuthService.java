package sms.uccbackend.domain.auth.authService;

import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sms.uccbackend.domain.auth.authDto.LoginRequest;
import sms.uccbackend.domain.auth.authDto.LoginResponse;
import sms.uccbackend.domain.auth.authDto.SignupRequest;
import sms.uccbackend.domain.user.userDto.UserResponse;
import sms.uccbackend.domain.user.userEntity.User;
import sms.uccbackend.domain.user.userRepository.UserRepository;
import sms.uccbackend.global.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final FirebaseTokenVerifier firebaseTokenVerifier;
    private final JwtTokenProvider jwtTokenProvider;

    // POST /auth/login - Firebase 토큰 검증 후 JWT 발급
    @Transactional
    public LoginResponse login(LoginRequest request) {
        FirebaseToken firebaseToken = firebaseTokenVerifier.verify(request.getFirebaseIdToken());

        String firebaseUid = firebaseToken.getUid();
        String email = firebaseToken.getEmail();
        String profileImage = firebaseToken.getPicture();

        boolean isNewUser = !userRepository.existsByFirebaseUid(firebaseUid);

        User user;
        if (isNewUser) {
            // 최초 로그인 시 임시 유저 생성 (nickname, schoolId는 signup에서 입력)
            user = User.builder()
                    .firebaseUid(firebaseUid)
                    .email(email)
                    .nickname(email.split("@")[0]) // 임시 닉네임
                    .profileImage(profileImage)
                    .build();
            userRepository.save(user);
        } else {
            user = userRepository.findByFirebaseUid(firebaseUid)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        }

        String accessToken = jwtTokenProvider.generateToken(user.getId());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .isNewUser(isNewUser)
                .user(UserResponse.from(user))
                .build();
    }

    // POST /auth/signup - 최초 회원가입 (학교/역할 선택)
    @Transactional
    public UserResponse signup(String firebaseIdToken, SignupRequest request) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
//
//        user.setNickname(request.getNickname());
//        user.setSchoolId(request.getSchoolId());
//        user.setBio(request.getBio());
//
//        return UserResponse.from(user);

        FirebaseToken firebaseToken = firebaseTokenVerifier.verify(firebaseIdToken);
        String firebaseUid = firebaseToken.getUid();

        User user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseGet(() -> {
                    // DB에 없을 때만 실행되는 블록 (새로 생성)
                    return User.builder()
                            .firebaseUid(firebaseUid)
                            .email(firebaseToken.getEmail())
                            .profileImage(firebaseToken.getPicture())
                            .build();
                });

        user.setNickname(request.getNickname());
        user.setSchoolId(request.getSchoolId());
        user.setBio(request.getBio());

        return UserResponse.from(user);
    }

    // GET /auth/me - 내 정보 조회
    public UserResponse getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        return UserResponse.from(user);
    }
}
