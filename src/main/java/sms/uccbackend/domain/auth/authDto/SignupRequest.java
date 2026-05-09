package sms.uccbackend.domain.auth.authDto;

import lombok.Getter;

@Getter
public class SignupRequest {
    private String nickname;
    private Long schoolId;
    private String bio;
}
