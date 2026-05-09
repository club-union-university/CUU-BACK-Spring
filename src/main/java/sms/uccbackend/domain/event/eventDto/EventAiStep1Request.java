package sms.uccbackend.domain.event.eventDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventAiStep1Request {
    private String rawInput;
}
