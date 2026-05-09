package sms.uccbackend.domain.event.eventDto;

import lombok.Getter;
import lombok.Setter;
import sms.uccbackend.domain.event.eventEntity.EventCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class EventUpdateRequest {
    private String title;
    private EventCategory category;
    private String description;
    private String format;

    private String locationName;
    private String locationAddress;
    private BigDecimal locationLat;
    private BigDecimal locationLng;
    private String placeId;
    private Long facilityId;

    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime recruitDeadline;
    private Integer maxParticipants;
}
