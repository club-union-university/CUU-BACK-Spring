package sms.uccbackend.domain.event.eventRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import sms.uccbackend.domain.event.eventEntity.Event;
import sms.uccbackend.domain.event.eventEntity.EventStatus;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByHostClubIdAndStatus(Long hostClubId, EventStatus status);

    List<Event> findByPartnerClubIdAndStatus(Long partnerClubId, EventStatus status);

    List<Event> findByHostClubId(Long hostClubId);

    List<Event> findByStatus(EventStatus status);
}
