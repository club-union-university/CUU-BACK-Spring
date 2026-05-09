package sms.uccbackend.domain.event.eventController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sms.uccbackend.domain.event.eventDto.*;
import sms.uccbackend.domain.event.eventEntity.EventCategory;
import sms.uccbackend.domain.event.eventEntity.EventStatus;
import sms.uccbackend.domain.event.eventEntity.EventType;
import sms.uccbackend.domain.event.eventService.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    // POST /events - 행사 생성
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @AuthenticationPrincipal Long userId,
            @RequestBody EventCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.createEvent(userId, request));
    }

    // GET /events - 행사 목록
    @GetMapping
    public ResponseEntity<List<EventResponse>> getEvents(
            @RequestParam(required = false) EventType type,
            @RequestParam(required = false) EventStatus status,
            @RequestParam(required = false) Long hostClubId,
            @RequestParam(required = false) EventCategory category
    ) {
        return ResponseEntity.ok(eventService.getEvents(type, status, hostClubId, category));
    }

    // GET /events/{eventId} - 행사 조회
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    // PATCH /events/{eventId} - 행사 수정
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long eventId,
            @RequestBody EventUpdateRequest request
    ) {
        return ResponseEntity.ok(eventService.updateEvent(userId, eventId, request));
    }

    // POST /events/{eventId}/submit - 파트너 검토 요청
    @PostMapping("/{eventId}/submit")
    public ResponseEntity<EventResponse> submitForReview(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.submitForReview(eventId));
    }

    // POST /events/{eventId}/approve - 행사 승인
    @PostMapping("/{eventId}/approve")
    public ResponseEntity<EventResponse> approveEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.approveEvent(eventId));
    }

    // POST /events/{eventId}/reject - 행사 거절
    @PostMapping("/{eventId}/reject")
    public ResponseEntity<EventResponse> rejectEvent(
            @PathVariable Long eventId,
            @RequestBody EventRejectRequest request
    ) {
        return ResponseEntity.ok(eventService.rejectEvent(eventId, request));
    }

    // POST /events/{eventId}/recruit - 모집 시작
    @PostMapping("/{eventId}/recruit")
    public ResponseEntity<EventResponse> startRecruiting(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.startRecruiting(eventId));
    }

    // POST /events/{eventId}/close - 행사 종료
    @PostMapping("/{eventId}/close")
    public ResponseEntity<EventResponse> closeEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.closeEvent(eventId));
    }

    // POST /events/{eventId}/participants - 참여 신청
    @PostMapping("/{eventId}/participants")
    public ResponseEntity<EventParticipantResponse> applyEvent(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long eventId,
            @RequestBody ParticipantApplyRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.applyEvent(userId, eventId, request));
    }

    // GET /events/{eventId}/participants - 참여자 목록
    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<EventParticipantResponse>> getParticipants(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getParticipants(eventId));
    }

    // PATCH /events/{eventId}/participants/{targetUserId} - 참여 승인/거절
    @PatchMapping("/{eventId}/participants/{targetUserId}")
    public ResponseEntity<EventParticipantResponse> respondParticipant(
            @PathVariable Long eventId,
            @PathVariable Long targetUserId,
            @RequestBody ParticipantRespondRequest request
    ) {
        return ResponseEntity.ok(eventService.respondParticipant(eventId, targetUserId, request));
    }
}
