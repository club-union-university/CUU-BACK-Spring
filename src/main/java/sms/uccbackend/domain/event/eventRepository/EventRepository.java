package sms.uccbackend.domain.event.eventRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sms.uccbackend.domain.event.eventEntity.Event;
import sms.uccbackend.domain.event.eventEntity.EventCategory;
import sms.uccbackend.domain.event.eventEntity.EventStatus;
import sms.uccbackend.domain.event.eventEntity.EventType;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByHostClubIdAndStatus(Long hostClubId, EventStatus status);

    List<Event> findByPartnerClubIdAndStatus(Long partnerClubId, EventStatus status);

    List<Event> findByHostClubId(Long hostClubId);

    List<Event> findByStatus(EventStatus status);

    @Query("SELECT e FROM Event e WHERE " +
            "(:type IS NULL OR e.type = :type) AND " +
            "(:status IS NULL OR e.status = :status) AND " +
            "(:hostClubId IS NULL OR e.hostClubId = :hostClubId) AND " +
            "(:category IS NULL OR e.category = :category) " +
            "ORDER BY e.createdAt DESC")
    List<Event> findByFilters(
            @Param("type") EventType type,
            @Param("status") EventStatus status,
            @Param("hostClubId") Long hostClubId,
            @Param("category") EventCategory category
    );

    // 모집 마감 임박(3일 이내) + 아직 미발송 + 모집 중
    @Query("SELECT e FROM Event e WHERE " +
            "e.status = 'RECRUITING' AND " +
            "e.recruitDeadline BETWEEN :now AND :threshold AND " +
            "e.deadlineReminderSentAt IS NULL")
    List<Event> findDeadlineApproachingNotReminded(
            @Param("now") LocalDateTime now,
            @Param("threshold") LocalDateTime threshold
    );

    // 당일 시작 + 아직 미발송 (오늘 시작 시각이 startOfDay~endOfDay 내)
    @Query("SELECT e FROM Event e WHERE " +
            "e.startAt BETWEEN :startOfDay AND :endOfDay AND " +
            "e.todayReminderSentAt IS NULL")
    List<Event> findStartingTodayNotReminded(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
