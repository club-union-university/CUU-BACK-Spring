package sms.uccbackend.domain.auth.authService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
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
public class FirebaseTokenVerifier {    public FirebaseToken verify(String idToken) {
    try {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    } catch (FirebaseAuthException e) {
        throw new IllegalArgumentException("유효하지 않은 Firebase 토큰입니다.");
    }
}

}
