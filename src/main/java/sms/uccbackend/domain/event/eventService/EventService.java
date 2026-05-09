package sms.uccbackend.domain.event.eventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sms.uccbackend.domain.club.clubEntity.Club;
import sms.uccbackend.domain.club.clubRepository.ClubRepository;
import sms.uccbackend.domain.event.eventDto.*;
import sms.uccbackend.domain.event.eventEntity.Event;
import sms.uccbackend.domain.event.eventEntity.EventCategory;
import sms.uccbackend.domain.event.eventEntity.EventParticipant;
import sms.uccbackend.domain.event.eventEntity.EventStatus;
import sms.uccbackend.domain.event.eventEntity.EventType;
import sms.uccbackend.domain.event.eventEntity.ParticipantStatus;
import sms.uccbackend.domain.event.eventRepository.EventParticipantRepository;
import sms.uccbackend.domain.event.eventRepository.EventRepository;
import sms.uccbackend.domain.notification.notificationEntity.NotificationType;
import sms.uccbackend.domain.notification.notificationService.NotificationService;
import sms.uccbackend.global.ai.AiClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final ClubRepository clubRepository;
    private final NotificationService notificationService;
    private final AiClient aiClient;

    // 행사 생성
    @Transactional
    public EventResponse createEvent(Long userId, EventCreateRequest request) {
        Event event = Event.builder()
                .type(request.getType())
                .hostClubId(request.getHostClubId())
                .partnerClubId(request.getPartnerClubId())
                .title(request.getTitle())
                .category(request.getCategory())
                .description(request.getDescription())
                .format(request.getFormat())
                .locationName(request.getLocationName())
                .locationAddress(request.getLocationAddress())
                .locationLat(request.getLocationLat())
                .locationLng(request.getLocationLng())
                .placeId(request.getPlaceId())
                .facilityId(request.getFacilityId())
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .recruitDeadline(request.getRecruitDeadline())
                .maxParticipants(request.getMaxParticipants())
                .proposalMessage(request.getProposalMessage())
                .status(EventStatus.DRAFT)
                .hostApproved(false)
                .partnerApproved(false)
                .createdByUserId(userId)
                .build();

        eventRepository.save(event);
        return EventResponse.from(event);
    }

    // 행사 조회
    public EventResponse getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 행사입니다."));
        return EventResponse.from(event);
    }

    // 행사 목록 조회
    public List<EventResponse> getEvents(EventType type, EventStatus status, Long hostClubId, EventCategory category) {
        return eventRepository.findByFilters(type, status, hostClubId, category)
                .stream()
                .map(EventResponse::from)
                .collect(Collectors.toList());
    }

    // 행사 수정
    @Transactional
    public EventResponse updateEvent(Long userId, Long eventId, EventUpdateRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 행사입니다."));

        if (!event.getCreatedByUserId().equals(userId)) {
            throw new IllegalArgumentException("행사 수정 권한이 없습니다.");
        }

        if (request.getTitle() != null) event.setTitle(request.getTitle());
        if (request.getCategory() != null) event.setCategory(request.getCategory());
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getFormat() != null) event.setFormat(request.getFormat());
        if (request.getLocationName() != null) event.setLocationName(request.getLocationName());
        if (request.getLocationAddress() != null) event.setLocationAddress(request.getLocationAddress());
        if (request.getLocationLat() != null) event.setLocationLat(request.getLocationLat());
        if (request.getLocationLng() != null) event.setLocationLng(request.getLocationLng());
        if (request.getPlaceId() != null) event.setPlaceId(request.getPlaceId());
        if (request.getFacilityId() != null) event.setFacilityId(request.getFacilityId());
        if (request.getStartAt() != null) event.setStartAt(request.getStartAt());
        if (request.getEndAt() != null) event.setEndAt(request.getEndAt());
        if (request.getRecruitDeadline() != null) event.setRecruitDeadline(request.getRecruitDeadline());
        if (request.getMaxParticipants() != null) event.setMaxParticipants(request.getMaxParticipants());

        return EventResponse.from(event);
    }

    // 파트너 검토 요청 (DRAFT → PARTNER_REVIEW)
    @Transactional
    public EventResponse submitForReview(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 행사입니다."));

        if (event.getStatus() != EventStatus.DRAFT) {
            throw new IllegalArgumentException("DRAFT 상태에서만 검토 요청이 가능합니다.");
        }

        event.setStatus(EventStatus.PARTNER_REVIEW);

        // 연합 행사면 파트너 동아리 회장에게 검토 요청 알림
        if (event.getType() == EventType.INTER_CLUB && event.getPartnerClubId() != null) {
            clubRepository.findById(event.getPartnerClubId()).ifPresent(partnerClub ->
                    notificationService.create(
                            partnerClub.getPresidentUserId(),
                            NotificationType.EVENT_PROPOSED,
                            "연합 행사 검토 요청",
                            String.format("'%s' 행사의 파트너 검토 요청이 도착했습니다.", event.getTitle()),
                            "/events/" + event.getId()
                    )
            );
        }

        return EventResponse.from(event);
    }

    // 행사 승인 (PARTNER_REVIEW → APPROVED)
    @Transactional
    public EventResponse approveEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 행사입니다."));

        event.setPartnerApproved(true);
        event.setStatus(EventStatus.APPROVED);
        event.setPartnerRespondedAt(LocalDateTime.now());

        notificationService.create(
                event.getCreatedByUserId(),
                NotificationType.EVENT_APPROVED,
                "행사가 승인되었습니다",
                String.format("'%s' 행사가 승인되어 모집을 시작할 수 있습니다.", event.getTitle()),
                "/events/" + event.getId()
        );

        return EventResponse.from(event);
    }

    // 행사 거절
    @Transactional
    public EventResponse rejectEvent(Long eventId, EventRejectRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 행사입니다."));

        event.setStatus(EventStatus.REJECTED);
        event.setRejectReason(request.getRejectReason());
        event.setPartnerRespondedAt(LocalDateTime.now());

        notificationService.create(
                event.getCreatedByUserId(),
                NotificationType.EVENT_REJECTED,
                "행사가 거절되었습니다",
                String.format("'%s' 행사가 거절되었습니다. 사유: %s", event.getTitle(), request.getRejectReason()),
                "/events/" + event.getId()
        );

        return EventResponse.from(event);
    }

    // 모집 시작 (APPROVED → RECRUITING)
    @Transactional
    public EventResponse startRecruiting(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 행사입니다."));

        if (event.getStatus() != EventStatus.APPROVED) {
            throw new IllegalArgumentException("APPROVED 상태에서만 모집을 시작할 수 있습니다.");
        }

        event.setStatus(EventStatus.RECRUITING);
        return EventResponse.from(event);
    }

    // 행사 종료 (RECRUITING → CLOSED)
    @Transactional
    public EventResponse closeEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 행사입니다."));

        event.setStatus(EventStatus.CLOSED);
        return EventResponse.from(event);
    }

    // 참여 신청
    @Transactional
    public EventParticipantResponse applyEvent(Long userId, Long eventId, ParticipantApplyRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 행사입니다."));

        if (event.getStatus() != EventStatus.RECRUITING) {
            throw new IllegalArgumentException("모집 중인 행사에만 신청할 수 있습니다.");
        }

        if (eventParticipantRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new IllegalArgumentException("이미 신청한 행사입니다.");
        }

        EventParticipant participant = EventParticipant.builder()
                .eventId(eventId)
                .userId(userId)
//                .participatingRole(request.getParticipatingRole())
                .status(ParticipantStatus.PENDING)
                .build();

        eventParticipantRepository.save(participant);

        // 호스트에게 참여 신청 알림 (본인 행사면 알림 불필요)
        if (!event.getCreatedByUserId().equals(userId)) {
            notificationService.create(
                    event.getCreatedByUserId(),
                    NotificationType.EVENT_PARTICIPANT_APPLIED,
                    "새 참여 신청이 도착했습니다",
                    String.format("'%s' 행사에 새 참여 신청이 도착했습니다.", event.getTitle()),
                    "/events/" + event.getId() + "/participants"
            );
        }

        return EventParticipantResponse.from(participant);
    }

    // 참여자 목록 조회
    public List<EventParticipantResponse> getParticipants(Long eventId) {
        return eventParticipantRepository.findByEventId(eventId)
                .stream()
                .map(EventParticipantResponse::from)
                .collect(Collectors.toList());
    }

    // 참여 승인/거절
    @Transactional
    public EventParticipantResponse respondParticipant(Long eventId, Long targetUserId, ParticipantRespondRequest request) {
        EventParticipant participant = eventParticipantRepository
                .findByEventIdAndUserId(eventId, targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("신청 내역이 없습니다."));

        participant.setStatus(request.getStatus());
        participant.setRespondedAt(LocalDateTime.now());

        Event event = eventRepository.findById(eventId).orElse(null);
        String eventTitle = event != null ? event.getTitle() : "행사";
        String resultMsg = request.getStatus() == ParticipantStatus.APPROVED ? "승인되었습니다" : "거절되었습니다";

        notificationService.create(
                targetUserId,
                NotificationType.EVENT_PARTICIPANT_RESPONDED,
                "참여 신청 결과",
                String.format("'%s' 행사 참여 신청이 %s.", eventTitle, resultMsg),
                "/events/" + eventId
        );

        return EventParticipantResponse.from(participant);
    }

    // AI Step 1: FE body를 Nest로 그대로 forwarding, 응답을 step1Data에 캐시
    @Transactional
    public Map<String, Object> runAiStep1(Long eventId, Map<String, Object> body) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 행사입니다."));

        log.info("[AI Step1] eventId={} | FE→Spring body={}", eventId, body);
        Map<String, Object> result = aiClient.step1(eventId, body);
        log.info("[AI Step1] eventId={} | Nest→Spring response={}", eventId, result);

        event.setStep1Data(result);
        return result;
    }

    // AI Step 2: FE body를 Nest로 그대로 forwarding, 응답을 step2Data에 캐시
    @Transactional
    public Map<String, Object> runAiStep2(Long eventId, Map<String, Object> body) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 행사입니다."));

        log.info("[AI Step2] eventId={} | FE→Spring body={}", eventId, body);
        Map<String, Object> result = aiClient.step2(eventId, body);
        log.info("[AI Step2] eventId={} | Nest→Spring response={}", eventId, result);

        event.setStep2Data(result);
        return result;
    }
}
