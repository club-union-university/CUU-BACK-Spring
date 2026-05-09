package sms.uccbackend.domain.notification.notificationEntity;

public enum NotificationType {
    // Club
    CLUB_APPROVED,
    CLUB_REJECTED,
    CLUB_MEMBER_JOINED,

    // Event
    EVENT_PROPOSED,
    EVENT_APPROVED,
    EVENT_REJECTED,
    EVENT_PARTICIPANT_APPLIED,
    EVENT_PARTICIPANT_RESPONDED,
    EVENT_DEADLINE_REMINDER,   // 모집 마감 3일 전
    EVENT_TODAY,               // 행사 당일

    // Post
    POST_NOTICE,               // 행사 공지글 등록

    // General
    GENERAL
}
