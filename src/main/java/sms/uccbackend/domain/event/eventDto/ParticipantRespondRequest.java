package sms.uccbackend.domain.event.eventDto;

import lombok.Getter;
import sms.uccbackend.domain.event.eventEntity.ParticipantStatus;

@Getter
public class ParticipantRespondRequest {
    private ParticipantStatus status;
}
