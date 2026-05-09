package sms.uccbackend.domain.user.userService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sms.uccbackend.domain.user.userDto.UserResponse;
import sms.uccbackend.domain.user.userDto.UserUpdateRequest;
import sms.uccbackend.domain.user.userEntity.User;
import sms.uccbackend.domain.user.userEntity.UserRole;
import sms.uccbackend.domain.user.userRepository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    // 사용자 프로필 조회
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        return UserResponse.from(user);
    }

    // 내 프로필 수정
    @Transactional
    public UserResponse updateMe(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        return UserResponse.from(user);
    }

    // 테스트용: 내 역할 변경 (SUPER_ADMIN / PRESIDENT / MEMBER)
    @Transactional
    public UserResponse updateMyRole(Long userId, UserRole role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        user.setRole(role);
        return UserResponse.from(user);
    }
}
