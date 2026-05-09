package sms.uccbackend.domain.club.clubController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sms.uccbackend.domain.club.clubDto.*;
import sms.uccbackend.domain.club.clubEntity.ClubCategory;
import sms.uccbackend.domain.club.clubEntity.ClubStatus;
import sms.uccbackend.domain.club.clubService.ClubService;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    // POST /clubs - 동아리 생성
    @PostMapping
    public ResponseEntity<ClubResponse> createClub(
            @AuthenticationPrincipal Long userId,
            @RequestBody ClubCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clubService.createClub(userId, request));
    }
    // GET /clubs - 동아리 목록
    @GetMapping
    public ResponseEntity<List<ClubResponse>> getClubs(
            @RequestParam(required = false) Long schoolId,
            @RequestParam(required = false) ClubCategory category,
            @RequestParam(required = false) ClubStatus status
    ) {
        return ResponseEntity.ok(clubService.getClubs(schoolId, category, status));
    }

    // GET /clubs/{clubId} - 동아리 조회
    @GetMapping("/{clubId}")
    public ResponseEntity<ClubResponse> getClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(clubService.getClub(clubId));
    }

    // PATCH /clubs/{clubId} - 동아리 수정
    @PatchMapping("/{clubId}")
    public ResponseEntity<ClubResponse> updateClub(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long clubId,
            @RequestBody ClubUpdateRequest request
    ) {
        return ResponseEntity.ok(clubService.updateClub(userId, clubId, request));
    }

    // POST /clubs/{clubId}/approve - 동아리 승인
    @PostMapping("/{clubId}/approve")
    public ResponseEntity<ClubResponse> approveClub(
            @AuthenticationPrincipal Long adminUserId,
            @PathVariable Long clubId
    ) {
        return ResponseEntity.ok(clubService.approveClub(adminUserId, clubId));
    }

    // POST /clubs/{clubId}/reject - 동아리 거절
    @PostMapping("/{clubId}/reject")
    public ResponseEntity<ClubResponse> rejectClub(
            @PathVariable Long clubId,
            @RequestBody ClubRejectRequest request
    ) {
        return ResponseEntity.ok(clubService.rejectClub(clubId, request));
    }

    // POST /clubs/join - 초대코드로 가입
    @PostMapping("/join")
    public ResponseEntity<ClubMemberResponse> joinClub(
            @AuthenticationPrincipal Long userId,
            @RequestBody ClubJoinRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clubService.joinClub(userId, request));
    }

    // GET /clubs/{clubId}/members - 부원 목록
    @GetMapping("/{clubId}/members")
    public ResponseEntity<List<ClubMemberResponse>> getMembers(@PathVariable Long clubId) {
        return ResponseEntity.ok(clubService.getMembers(clubId));
    }

    // DELETE /clubs/{clubId}/members/{userId} - 강퇴/탈퇴
    @DeleteMapping("/{clubId}/members/{targetUserId}")
    public ResponseEntity<Void> removeMember(
            @AuthenticationPrincipal Long requestUserId,
            @PathVariable Long clubId,
            @PathVariable Long targetUserId
    ) {
        clubService.removeMember(requestUserId, clubId, targetUserId);
        return ResponseEntity.noContent().build();
    }
}
