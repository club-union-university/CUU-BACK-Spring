package sms.uccbackend.domain.auth.authDto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String firebaseIdToken;
}
