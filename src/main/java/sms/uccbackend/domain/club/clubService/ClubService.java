package sms.uccbackend.domain.club.clubService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sms.uccbackend.domain.club.clubDto.*;
import sms.uccbackend.domain.club.clubEntity.*;
import sms.uccbackend.domain.club.clubRepository.ClubMemberRepository;
import sms.uccbackend.domain.club.clubRepository.ClubRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    // 동아리 생성
    @Transactional
    public ClubResponse createClub(Long userId, ClubCreateRequest request) {
        if (clubRepository.existsBySchoolIdAndName(request.getSchoolId(), request.getName())) {
            throw new IllegalArgumentException("이미 존재하는 동아리 이름입니다.");
        }

        Club club = Club.builder()
                .schoolId(request.getSchoolId())
                .presidentUserId(userId)
                .name(request.getName())
                .category(request.getCategory())
                .description(request.getDescription())
                .logoImage(request.getLogoImage())
                .evidenceUrl(request.getEvidenceUrl())
                .inviteCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .status(ClubStatus.PENDING)
                .build();

        clubRepository.save(club);

        // 생성자를 PRESIDENT로 자동 가입
        ClubMember president = ClubMember.builder()
                .clubId(club.getId())
                .userId(userId)
                .memberRole(ClubMemberRole.PRESIDENT)
                .status(ClubMemberStatus.APPROVED)
                .build();

        clubMemberRepository.save(president);

        return ClubResponse.from(club);
    }

    // 동아리 조회
    public ClubResponse getClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동아리입니다."));
        return ClubResponse.from(club);
    }

    // 동아리 수정
    @Transactional
    public ClubResponse updateClub(Long userId, Long clubId, ClubUpdateRequest request) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동아리입니다."));

        if (!club.getPresidentUserId().equals(userId)) {
            throw new IllegalArgumentException("동아리 수정 권한이 없습니다.");
        }

        if (request.getCategory() != null) club.setCategory(request.getCategory());
        if (request.getDescription() != null) club.setDescription(request.getDescription());
        if (request.getLogoImage() != null) club.setLogoImage(request.getLogoImage());
        if (request.getEvidenceUrl() != null) club.setEvidenceUrl(request.getEvidenceUrl());

        return ClubResponse.from(club);
    }

    // 동아리 승인 (SUPER_ADMIN)
    @Transactional
    public ClubResponse approveClub(Long adminUserId, Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동아리입니다."));

        club.setStatus(ClubStatus.APPROVED);
        club.setApprovedByUserId(adminUserId);
        club.setApprovedAt(LocalDateTime.now());
        club.setRejectReason(null);

        return ClubResponse.from(club);
    }

    // 동아리 거절 (SUPER_ADMIN)
    @Transactional
    public ClubResponse rejectClub(Long clubId, ClubRejectRequest request) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동아리입니다."));

        club.setStatus(ClubStatus.REJECTED);
        club.setRejectReason(request.getRejectReason());

        return ClubResponse.from(club);
    }

    // 초대코드로 가입
    @Transactional
    public ClubMemberResponse joinClub(Long userId, ClubJoinRequest request) {
        Club club = clubRepository.findByInviteCode(request.getInviteCode())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 초대코드입니다."));

        if (club.getStatus() != ClubStatus.APPROVED) {
            throw new IllegalArgumentException("승인된 동아리만 가입할 수 있습니다.");
        }

        if (clubMemberRepository.existsByClubIdAndUserId(club.getId(), userId)) {
            throw new IllegalArgumentException("이미 가입된 동아리입니다.");
        }

        ClubMember member = ClubMember.builder()
                .clubId(club.getId())
                .userId(userId)
                .memberRole(ClubMemberRole.MEMBER)
                .status(ClubMemberStatus.APPROVED)
                .build();

        clubMemberRepository.save(member);

        return ClubMemberResponse.from(member);
    }

    // 부원 목록 조회
    public List<ClubMemberResponse> getMembers(Long clubId) {
        return clubMemberRepository.findByClubIdAndStatus(clubId, ClubMemberStatus.APPROVED)
                .stream()
                .map(ClubMemberResponse::from)
                .collect(Collectors.toList());
    }

    // 부원 강퇴/탈퇴
    @Transactional
    public void removeMember(Long requestUserId, Long clubId, Long targetUserId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동아리입니다."));

        // 본인 탈퇴 or 회장이 강퇴
        boolean isSelf = requestUserId.equals(targetUserId);
        boolean isPresident = club.getPresidentUserId().equals(requestUserId);

        if (!isSelf && !isPresident) {
            throw new IllegalArgumentException("강퇴 권한이 없습니다.");
        }

        ClubMember member = clubMemberRepository.findByClubIdAndUserId(clubId, targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 부원이 존재하지 않습니다."));

        member.setStatus(ClubMemberStatus.LEFT);
    }

    public List<ClubResponse> getClubs(Long userId, Long schoolId, ClubCategory category, ClubStatus status) {
        return clubRepository.findJoinedByUserAndFilters(userId, schoolId, category, status)
                .stream()
                .map(ClubResponse::from)
                .collect(Collectors.toList());
    }
}
