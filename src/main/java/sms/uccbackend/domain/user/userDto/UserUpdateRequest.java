package sms.uccbackend.domain.user.userDto;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private String nickname;
    private String profileImage;
    private String bio;
}
