package sms.uccbackend.domain.event.eventDto;

import lombok.Getter;
import sms.uccbackend.domain.event.eventEntity.EventCategory;
import sms.uccbackend.domain.event.eventEntity.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class EventCreateRequest {
    private EventType type;
    private Long hostClubId;
    private Long partnerClubId;
    private String title;
    private EventCategory category;
    private String description;
    private String format;

    // 장소
    private String locationName;
    private String locationAddress;
    private BigDecimal locationLat;
    private BigDecimal locationLng;
    private String placeId;
    private Long facilityId;

    // 일정
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime recruitDeadline;
    private Integer maxParticipants;

    // INTER_CLUB 제안 메시지
    private String proposalMessage;
}
