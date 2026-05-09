package sms.uccbackend.domain.school.schoolController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sms.uccbackend.domain.school.schoolService.SchoolService;
import sms.uccbackend.domain.school.shcoolDto.SchoolFacilityCreateRequest;
import sms.uccbackend.domain.school.shcoolDto.SchoolFacilityResponse;
import sms.uccbackend.domain.school.shcoolDto.SchoolResponse;

import java.util.List;

@RestController
@RequestMapping("api/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;

    // GET /schools - 학교 목록 조회
    @GetMapping
    public ResponseEntity<List<SchoolResponse>> getSchools() {
        return ResponseEntity.ok(schoolService.getSchools());
    }

    // GET /schools/{schoolId} - 학교 단건 조회
    @GetMapping("/{schoolId}")
    public ResponseEntity<SchoolResponse> getSchool(@PathVariable Long schoolId) {
        return ResponseEntity.ok(schoolService.getSchool(schoolId));
    }

    // GET /schools/{schoolId}/facilities - 시설 목록 조회
    @GetMapping("/{schoolId}/facilities")
    public ResponseEntity<List<SchoolFacilityResponse>> getFacilities(@PathVariable Long schoolId) {
        return ResponseEntity.ok(schoolService.getFacilities(schoolId));
    }

    // POST /schools/{schoolId}/facilities - 시설 등록
    @PostMapping("/{schoolId}/facilities")
    public ResponseEntity<SchoolFacilityResponse> createFacility(
            @PathVariable Long schoolId,
            @RequestBody SchoolFacilityCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(schoolService.createFacility(schoolId, request));
    }
}
