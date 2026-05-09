package sms.uccbackend.domain.school.schoolController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/shcools")
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;

    @Operation(summary = "학교 목록 (회원가입 시 사용)")
    @GetMapping
    public ResponseEntity<List<SchoolResponse>> getSchools(
            @RequestParam(required = false) Region region,
            @RequestParam(defaultValue = "true") boolean whitelistedOnly) {

        List<SchoolResponse> schools = schoolService.getSchools(region, whitelistedOnly);
        return ResponseEntity.ok(schools);
    }

    @Operation(summary = "학교 상세")
    @GetMapping("/{id}")
    public ResponseEntity<SchoolResponse> getSchool(@PathVariable Long id) {
        SchoolResponse school = schoolService.getSchoolById(id);
        return ResponseEntity.ok(school);
    }

    @Operation(summary = "교내 시설 목록")
    @GetMapping("/{id}/facilities")
    public ResponseEntity<List<SchoolFacilityResponse>> getSchoolFacilities(
            @PathVariable Long id,
            @RequestParam(required = false) FacilityType facilityType) {

        List<SchoolFacilityResponse> facilities = schoolService.getFacilitiesBySchoolId(id, facilityType);
        return ResponseEntity.ok(facilities);
    }

}
