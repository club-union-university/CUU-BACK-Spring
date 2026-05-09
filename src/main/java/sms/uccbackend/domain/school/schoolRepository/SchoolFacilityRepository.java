package sms.uccbackend.domain.school.schoolRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import sms.uccbackend.domain.school.schoolEntity.FacilityType;
import sms.uccbackend.domain.school.schoolEntity.SchoolFacility;

import java.util.List;

public interface SchoolFacilityRepository extends JpaRepository<SchoolFacility, Long> {
    List<SchoolFacility> findBySchoolId(Long schoolId);

    List<SchoolFacility> findBySchoolIdAndFacilityType(Long schoolId, FacilityType facilityType);
}
