package sms.uccbackend.domain.club.clubRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import sms.uccbackend.domain.club.clubEntity.ClubMember;
import sms.uccbackend.domain.club.clubEntity.ClubMemberStatus;

import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    Optional<ClubMember> findByClubIdAndUserId(Long clubId, Long userId);

    boolean existsByClubIdAndUserId(Long clubId, Long userId);

    List<ClubMember> findByClubIdAndStatus(Long clubId, ClubMemberStatus status);

    List<ClubMember> findByUserIdAndStatus(Long userId, ClubMemberStatus status);
}
