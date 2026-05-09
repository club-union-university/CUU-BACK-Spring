package sms.uccbackend.domain.school.shcoolDto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import sms.uccbackend.domain.school.schoolEntity.FacilityType;
import sms.uccbackend.domain.school.schoolEntity.SchoolFacility;

import java.time.LocalDateTime;

@Builder
@Getter
public class SchoolFacilityResponse {
    private Long id;
    private Long schoolId;
    private String name;
    private FacilityType facilityType;
    private Integer capacity;
    private BigDecimal lat;
    private BigDecimal lng;
    private LocalDateTime createdAt;

    public static SchoolFacilityResponse from(SchoolFacility facility) {
        return SchoolFacilityResponse.builder()
                .id(facility.getId())
                .schoolId(facility.getSchoolId())
                .name(facility.getName())
                .facilityType(facility.getFacilityType())
                .capacity(facility.getCapacity())
                .lat(facility.getLat())
                .lng(facility.getLng())
                .createdAt(facility.getCreatedAt())
                .build();
    }
}
