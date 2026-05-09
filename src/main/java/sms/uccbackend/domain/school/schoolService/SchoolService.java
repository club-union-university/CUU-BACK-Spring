package sms.uccbackend.domain.school.schoolService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sms.uccbackend.domain.school.schoolEntity.School;
import sms.uccbackend.domain.school.schoolEntity.SchoolFacility;
import sms.uccbackend.domain.school.schoolRepository.SchoolFacilityRepository;
import sms.uccbackend.domain.school.schoolRepository.SchoolRepository;
import sms.uccbackend.domain.school.shcoolDto.SchoolFacilityCreateRequest;
import sms.uccbackend.domain.school.shcoolDto.SchoolFacilityResponse;
import sms.uccbackend.domain.school.shcoolDto.SchoolResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchoolService {
    private final SchoolRepository schoolRepository;
    private final SchoolFacilityRepository schoolFacilityRepository;

    // 학교 목록 조회 (경인권만)
    public List<SchoolResponse> getSchools() {
        return schoolRepository.findByIsWhitelisted(true)
                .stream()
                .map(SchoolResponse::from)
                .collect(Collectors.toList());
    }

    // 학교 단건 조회
    public SchoolResponse getSchool(Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학교입니다."));
        return SchoolResponse.from(school);
    }

    // 시설 목록 조회
    public List<SchoolFacilityResponse> getFacilities(Long schoolId) {
        schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학교입니다."));
        return schoolFacilityRepository.findBySchoolId(schoolId)
                .stream()
                .map(SchoolFacilityResponse::from)
                .collect(Collectors.toList());
    }

    // 시설 등록
    @Transactional
    public SchoolFacilityResponse createFacility(Long schoolId, SchoolFacilityCreateRequest request) {
        schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학교입니다."));

        SchoolFacility facility = SchoolFacility.builder()
                .schoolId(schoolId)
                .name(request.getName())
                .facilityType(request.getFacilityType())
                .capacity(request.getCapacity())
                .lat(request.getLat())
                .lng(request.getLng())
                .build();

        schoolFacilityRepository.save(facility);

        return SchoolFacilityResponse.from(facility);
    }
}
