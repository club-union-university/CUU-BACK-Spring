package sms.uccbackend.domain.club.clubEntity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "clubs",
        uniqueConstraints = @UniqueConstraint(columnNames = {"school_id", "name"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long schoolId;

    @Column(nullable = false)
    private Long presidentUserId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ClubCategory category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(unique = true, nullable = false)
    private String inviteCode;

    private String logoImage;

    private String evidenceUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ClubStatus status = ClubStatus.PENDING;

    private Long approvedByUserId;

    private LocalDateTime approvedAt;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String rejectReason;

    public void approve(Long adminUserId) {
        this.status = ClubStatus.APPROVED;
        this.approvedByUserId = adminUserId;
        this.approvedAt = LocalDateTime.now();
        this.rejectReason = null;
    }

    public void reject(String reason) {
        this.status = ClubStatus.REJECTED;
        this.rejectReason = reason;
    }

    public void update(String description, String logoImage, String evidenceUrl, ClubCategory category) {
        if (description != null) this.description = description;
        if (logoImage != null) this.logoImage = logoImage;
        if (evidenceUrl != null) this.evidenceUrl = evidenceUrl;
        if (category != null) this.category = category;
    }
}
