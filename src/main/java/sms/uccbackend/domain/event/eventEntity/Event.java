package sms.uccbackend.domain.event.eventEntity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    @Column(nullable = false)
    private Long hostClubId;

    private Long partnerClubId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private EventCategory category;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String format;

    // AI 정제 결과 캐시
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> step1Data;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> step2Data;

    // 장소
    private String locationName;
    private String locationAddress;

    @Column(precision = 10, scale = 7)
    private BigDecimal locationLat;

    @Column(precision = 10, scale = 7)
    private BigDecimal locationLng;

    private String placeId;
    private Long facilityId;

    // 일정
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime recruitDeadline;
    private Integer maxParticipants;

    // 승인 흐름
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EventStatus status = EventStatus.DRAFT;

    @Builder.Default
    private Boolean hostApproved = false;

    @Builder.Default
    private Boolean partnerApproved = false;

    @Column(columnDefinition = "TEXT")
    private String proposalMessage;

    @Column(columnDefinition = "TEXT")
    private String rejectReason;

    private LocalDateTime partnerRespondedAt;

    @Column(nullable = false)
    private Long createdByUserId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
