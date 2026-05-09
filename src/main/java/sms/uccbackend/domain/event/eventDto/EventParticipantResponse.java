package sms.uccbackend.domain.event.eventDto;

import lombok.Builder;
import lombok.Getter;
import sms.uccbackend.domain.event.eventEntity.EventParticipant;
import sms.uccbackend.domain.event.eventEntity.ParticipantStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventParticipantResponse {
    private Long id;
    private Long eventId;
    private Long userId;
    //    private PersonalRole participatingRole;
    private ParticipantStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime respondedAt;

    public static EventParticipantResponse from(EventParticipant participant) {
        return EventParticipantResponse.builder()
                .id(participant.getId())
                .eventId(participant.getEventId())
                .userId(participant.getUserId())
//                .participatingRole(participant.getParticipatingRole())
                .status(participant.getStatus())
                .appliedAt(participant.getAppliedAt())
                .respondedAt(participant.getRespondedAt())
                .build();

    }
}