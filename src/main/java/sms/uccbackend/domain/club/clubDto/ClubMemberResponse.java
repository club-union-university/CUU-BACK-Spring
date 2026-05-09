package sms.uccbackend.domain.club.clubDto;

import lombok.Builder;
import lombok.Getter;
import sms.uccbackend.domain.club.clubEntity.ClubMember;
import sms.uccbackend.domain.club.clubEntity.ClubMemberRole;
import sms.uccbackend.domain.club.clubEntity.ClubMemberStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClubMemberResponse {
    private Long id;
    private Long clubId;
    private Long userId;
    private ClubMemberRole memberRole;
    private ClubMemberStatus status;
    private LocalDateTime joinedAt;

    public static ClubMemberResponse from(ClubMember clubMember) {
        return ClubMemberResponse.builder()
                .id(clubMember.getId())
                .clubId(clubMember.getClubId())
                .userId(clubMember.getUserId())
                .memberRole(clubMember.getMemberRole())
                .status(clubMember.getStatus())
                .joinedAt(clubMember.getJoinedAt())
                .build();
    }
}
