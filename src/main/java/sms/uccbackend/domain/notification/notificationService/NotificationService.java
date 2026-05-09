package sms.uccbackend.domain.notification.notificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sms.uccbackend.domain.notification.notificationDto.NotificationResponse;
import sms.uccbackend.domain.notification.notificationEntity.Notification;
import sms.uccbackend.domain.notification.notificationEntity.NotificationType;
import sms.uccbackend.domain.notification.notificationRepository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;

    // 내 알림 목록
    public List<NotificationResponse> getMyNotifications(Long userId, boolean unreadOnly) {
        List<Notification> list = unreadOnly
                ? notificationRepository.findByRecipientUserIdAndIsReadOrderByCreatedAtDesc(userId, false)
                : notificationRepository.findByRecipientUserIdOrderByCreatedAtDesc(userId);

        return list.stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    // 단건 읽음 처리
    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다."));

        if (!n.getRecipientUserId().equals(userId)) {
            throw new IllegalArgumentException("본인의 알림만 읽음 처리할 수 있습니다.");
        }

        if (!n.getIsRead()) {
            n.setIsRead(true);
            n.setReadAt(LocalDateTime.now());
        }
    }

    // 전체 읽음 처리
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId, LocalDateTime.now());
    }

    // 다른 서비스에서 알림 생성할 때 호출하는 헬퍼
    @Transactional
    public Notification create(Long recipientUserId, NotificationType type, String title, String content, String linkUrl) {
        Notification n = Notification.builder()
                .recipientUserId(recipientUserId)
                .type(type)
                .title(title)
                .content(content)
                .linkUrl(linkUrl)
                .isRead(false)
                .build();
        return notificationRepository.save(n);
    }
}
