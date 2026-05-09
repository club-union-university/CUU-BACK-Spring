package sms.uccbackend.domain.user.userController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sms.uccbackend.domain.user.userDto.UserResponse;
import sms.uccbackend.domain.user.userDto.UserUpdateRequest;
import sms.uccbackend.domain.user.userService.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // GET /users/{id} - 사용자 프로필 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    // PATCH /users/me - 내 프로필 수정
    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.updateMe(userId, request));
    }

}
