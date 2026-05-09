package sms.uccbackend.domain.user.userDto;

import lombok.Getter;
import sms.uccbackend.domain.user.userEntity.UserRole;

@Getter
public class UserUpdateRequest {
    private String nickname;
    private String profileImage;
    private String bio;
//    private UserRole role;
}
