package sms.uccbackend.domain.school.shcoolDto;

import java.math.BigDecimal;
import lombok.Getter;
import sms.uccbackend.domain.school.schoolEntity.FacilityType;

@Getter
public class SchoolFacilityCreateRequest {
    private String name;
    private FacilityType facilityType;
    private Integer capacity;
    private BigDecimal lat;
    private BigDecimal lng;
}
