package sms.uccbackend.domain.event.eventService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sms.uccbackend.domain.event.eventDto.*;
import sms.uccbackend.domain.event.eventEntity.Event;
import sms.uccbackend.domain.event.eventEntity.EventCategory;
import sms.uccbackend.domain.event.eventEntity.EventParticipant;
import sms.uccbackend.domain.event.eventEntity.EventStatus;
import sms.uccbackend.domain.event.eventEntity.EventType;
import sms.uccbackend.domain.event.eventEntity.ParticipantStatus;
import sms.uccbackend.domain.event.eventRepository.EventParticipantRepository;
import sms.uccbackend.domain.event.eventRepository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;

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

        return EventParticipantResponse.from(participant);
    }
}
