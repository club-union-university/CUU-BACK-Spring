package sms.uccbackend.domain.school.shcoolDto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import sms.uccbackend.domain.school.schoolEntity.CampusType;
import sms.uccbackend.domain.school.schoolEntity.Region;
import sms.uccbackend.domain.school.schoolEntity.School;

import java.time.LocalDateTime;

@Builder
@Getter
public class SchoolResponse {
    private Long id;
    private String name;
    private String emailDomain;
    private Region region;
    private CampusType campusType;
    private BigDecimal lat;
    private BigDecimal lng;
    private String mascotImage;
    private Boolean isWhitelisted;
    private LocalDateTime createdAt;

    public static SchoolResponse from(School school) {
        return SchoolResponse.builder()
                .id(school.getId())
                .name(school.getName())
                .emailDomain(school.getEmailDomain())
                .region(school.getRegion())
                .campusType(school.getCampusType())
                .lat(school.getLat())
                .lng(school.getLng())
                .mascotImage(school.getMascotImage())
                .isWhitelisted(school.getIsWhitelisted())
                .createdAt(school.getCreatedAt())
                .build();
    }
}
