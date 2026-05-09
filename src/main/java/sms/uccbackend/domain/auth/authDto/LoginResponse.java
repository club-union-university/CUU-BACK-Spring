package sms.uccbackend.domain.auth.authDto;

import lombok.Builder;
import lombok.Getter;
import sms.uccbackend.domain.user.userDto.UserResponse;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    private boolean isNewUser;
    private UserResponse user;
}
