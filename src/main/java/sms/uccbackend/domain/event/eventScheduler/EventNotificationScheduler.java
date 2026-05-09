package sms.uccbackend.domain.event.eventScheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sms.uccbackend.domain.event.eventEntity.Event;
import sms.uccbackend.domain.event.eventEntity.EventParticipant;
import sms.uccbackend.domain.event.eventEntity.ParticipantStatus;
import sms.uccbackend.domain.event.eventRepository.EventParticipantRepository;
import sms.uccbackend.domain.event.eventRepository.EventRepository;
import sms.uccbackend.domain.notification.notificationEntity.NotificationType;
import sms.uccbackend.domain.notification.notificationService.NotificationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventNotificationScheduler {

    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final NotificationService notificationService;

    // 매일 오전 9시: 모집 마감 3일 이내 행사의 참여자에게 알림
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void notifyDeadlineApproaching() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.plusDays(3);

        List<Event> events = eventRepository.findDeadlineApproachingNotReminded(now, threshold);
        log.info("[Scheduler] 마감 임박 행사 {}개", events.size());

        for (Event event : events) {
            List<EventParticipant> participants = eventParticipantRepository
                    .findByEventIdAndStatus(event.getId(), ParticipantStatus.APPROVED);

            for (EventParticipant p : participants) {
                notificationService.create(
                        p.getUserId(),
                        NotificationType.EVENT_DEADLINE_REMINDER,
                        "행사 마감이 임박했습니다",
                        String.format("'%s' 행사 모집이 곧 마감됩니다.", event.getTitle()),
                        "/events/" + event.getId()
                );
            }
            event.setDeadlineReminderSentAt(now);
        }
    }

    // 매일 오전 9시: 당일 시작 행사의 참여자에게 알림
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void notifyEventToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<Event> events = eventRepository.findStartingTodayNotReminded(startOfDay, endOfDay);
        log.info("[Scheduler] 당일 행사 {}개", events.size());

        LocalDateTime now = LocalDateTime.now();
        for (Event event : events) {
            List<EventParticipant> participants = eventParticipantRepository
                    .findByEventIdAndStatus(event.getId(), ParticipantStatus.APPROVED);

            for (EventParticipant p : participants) {
                notificationService.create(
                        p.getUserId(),
                        NotificationType.EVENT_TODAY,
                        "오늘은 행사 당일입니다",
                        String.format("'%s' 행사가 오늘 진행됩니다.", event.getTitle()),
                        "/events/" + event.getId()
                );
            }
            event.setTodayReminderSentAt(now);
        }
    }
}
