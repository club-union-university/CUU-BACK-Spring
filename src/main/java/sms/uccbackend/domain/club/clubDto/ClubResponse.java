package sms.uccbackend.domain.club.clubDto;

import lombok.Builder;
import lombok.Getter;
import sms.uccbackend.domain.club.clubEntity.Club;
import sms.uccbackend.domain.club.clubEntity.ClubCategory;
import sms.uccbackend.domain.club.clubEntity.ClubStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClubResponse {
    private Long id;
    private Long schoolId;
    private Long presidentUserId;
    private String name;
    private ClubCategory category;
    private String description;
    private String inviteCode;
    private String logoImage;
    private String evidenceUrl;
    private ClubStatus status;
    private String rejectReason;
    private LocalDateTime createdAt;

    public static ClubResponse from(Club club) {
        return ClubResponse.builder()
                .id(club.getId())
                .schoolId(club.getSchoolId())
                .presidentUserId(club.getPresidentUserId())
                .name(club.getName())
                .category(club.getCategory())
                .description(club.getDescription())
                .inviteCode(club.getInviteCode())
                .logoImage(club.getLogoImage())
                .evidenceUrl(club.getEvidenceUrl())
                .status(club.getStatus())
                .rejectReason(club.getRejectReason())
                .createdAt(club.getCreatedAt())
                .build();
    }
}
