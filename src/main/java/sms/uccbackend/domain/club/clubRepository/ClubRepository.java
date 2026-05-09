package sms.uccbackend.domain.club.clubRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import sms.uccbackend.domain.club.clubEntity.Club;
import sms.uccbackend.domain.club.clubEntity.ClubStatus;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByInviteCode(String inviteCode);

    boolean existsBySchoolIdAndName(Long schoolId, String name);

    List<Club> findBySchoolIdAndStatus(Long schoolId, ClubStatus status);

    List<Club> findByPresidentUserId(Long presidentUserId);

}
