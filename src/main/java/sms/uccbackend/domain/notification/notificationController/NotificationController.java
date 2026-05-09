package sms.uccbackend.domain.notification.notificationController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sms.uccbackend.domain.notification.notificationDto.NotificationResponse;
import sms.uccbackend.domain.notification.notificationService.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // GET /notifications?unreadOnly=true|false - 내 알림 목록
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean unreadOnly
    ) {
        return ResponseEntity.ok(notificationService.getMyNotifications(userId, unreadOnly));
    }

    // PATCH /notifications/read-all - 전체 읽음 처리
    // 주의: /{id}/read 보다 위에 등록 (정적 경로 우선)
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    // PATCH /notifications/{id}/read - 알림 읽음 처리
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long id
    ) {
        notificationService.markAsRead(userId, id);
        return ResponseEntity.noContent().build();
    }
}
