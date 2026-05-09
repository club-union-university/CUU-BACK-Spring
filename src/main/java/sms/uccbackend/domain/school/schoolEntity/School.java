package sms.uccbackend.domain.school.schoolEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shcools")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class School{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String emailDomain;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region region;

    @Enumerated(EnumType.STRING)
    private CampusType campusType;

    @Column(precision = 10, scale = 7)
    private BigDecimal lat;

    @Column(precision = 10, scale = 7)
    private BigDecimal lng;

    private String mascotImage;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isWhitelisted = false;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
