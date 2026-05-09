package sms.uccbackend.domain.club.clubEntity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "club_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"club_id", "user_id"})
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ClubMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clubId;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ClubMemberRole memberRole = ClubMemberRole.MEMBER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ClubMemberStatus status = ClubMemberStatus.APPROVED;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime joinedAt;
}
