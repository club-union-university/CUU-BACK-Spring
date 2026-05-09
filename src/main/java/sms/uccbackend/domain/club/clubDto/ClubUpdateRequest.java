package sms.uccbackend.domain.club.clubDto;

import lombok.Getter;
import sms.uccbackend.domain.club.clubEntity.ClubCategory;

@Getter
public class ClubUpdateRequest {
    private ClubCategory category;
    private String description;
    private String logoImage;
    private String evidenceUrl;
}
