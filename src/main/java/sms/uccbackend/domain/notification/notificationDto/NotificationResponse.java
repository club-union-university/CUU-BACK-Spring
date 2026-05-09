package sms.uccbackend.domain.notification.notificationDto;

import lombok.Builder;
import lombok.Getter;
import sms.uccbackend.domain.notification.notificationEntity.Notification;
import sms.uccbackend.domain.notification.notificationEntity.NotificationType;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {
    private Long id;
    private Long recipientUserId;
    private NotificationType type;
    private String title;
    private String content;
    private String linkUrl;
    private Boolean isRead;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .recipientUserId(n.getRecipientUserId())
                .type(n.getType())
                .title(n.getTitle())
                .content(n.getContent())
                .linkUrl(n.getLinkUrl())
                .isRead(n.getIsRead())
                .readAt(n.getReadAt())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
