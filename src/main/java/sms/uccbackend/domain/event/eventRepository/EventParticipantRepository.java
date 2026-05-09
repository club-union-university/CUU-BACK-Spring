package sms.uccbackend.domain.event.eventRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import sms.uccbackend.domain.event.eventEntity.EventParticipant;
import sms.uccbackend.domain.event.eventEntity.ParticipantStatus;

import java.util.List;
import java.util.Optional;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {

    Optional<EventParticipant> findByEventIdAndUserId(Long eventId, Long userId);

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    List<EventParticipant> findByEventId(Long eventId);

    List<EventParticipant> findByEventIdAndStatus(Long eventId, ParticipantStatus status);

    List<EventParticipant> findByUserId(Long userId);
}
