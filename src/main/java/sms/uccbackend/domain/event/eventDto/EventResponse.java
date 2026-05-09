package sms.uccbackend.domain.event.eventDto;

import lombok.Builder;
import lombok.Getter;
import sms.uccbackend.domain.event.eventEntity.Event;
import sms.uccbackend.domain.event.eventEntity.EventCategory;
import sms.uccbackend.domain.event.eventEntity.EventStatus;
import sms.uccbackend.domain.event.eventEntity.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class EventResponse {
    private Long id;
    private EventType type;
    private Long hostClubId;
    private Long partnerClubId;
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

    private EventStatus status;
    private Boolean hostApproved;
    private Boolean partnerApproved;
    private String proposalMessage;
    private String rejectReason;

    private Long createdByUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EventResponse from(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .type(event.getType())
                .hostClubId(event.getHostClubId())
                .partnerClubId(event.getPartnerClubId())
                .title(event.getTitle())
                .category(event.getCategory())
                .description(event.getDescription())
                .format(event.getFormat())
                .locationName(event.getLocationName())
                .locationAddress(event.getLocationAddress())
                .locationLat(event.getLocationLat())
                .locationLng(event.getLocationLng())
                .placeId(event.getPlaceId())
                .facilityId(event.getFacilityId())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .recruitDeadline(event.getRecruitDeadline())
                .maxParticipants(event.getMaxParticipants())
                .status(event.getStatus())
                .hostApproved(event.getHostApproved())
                .partnerApproved(event.getPartnerApproved())
                .proposalMessage(event.getProposalMessage())
                .rejectReason(event.getRejectReason())
                .createdByUserId(event.getCreatedByUserId())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}
