package sms.uccbackend.domain.user.userDto;

import lombok.Builder;
import lombok.Getter;
import sms.uccbackend.domain.user.userEntity.User;
import sms.uccbackend.domain.user.userEntity.UserRole;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String firebaseUid;
    private String email;
    private String nickname;
    private String profileImage;
    private Long schoolId;
    private String bio;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firebaseUid(user.getFirebaseUid())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .schoolId(user.getSchoolId())
                .bio(user.getBio())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
