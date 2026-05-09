package sms.uccbackend.domain.club.clubRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sms.uccbackend.domain.club.clubEntity.Club;
import sms.uccbackend.domain.club.clubEntity.ClubCategory;
import sms.uccbackend.domain.club.clubEntity.ClubStatus;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByInviteCode(String inviteCode);

    boolean existsBySchoolIdAndName(Long schoolId, String name);

    List<Club> findBySchoolIdAndStatus(Long schoolId, ClubStatus status);

    List<Club> findByPresidentUserId(Long presidentUserId);

    List<Club> findBySchoolIdAndCategoryAndStatus(Long schoolId, ClubCategory category, ClubStatus status);

    List<Club> findBySchoolId(Long schoolId);

    @Query("SELECT c FROM Club c WHERE " +
            "(:schoolId IS NULL OR c.schoolId = :schoolId) AND " +
            "(:category IS NULL OR c.category = :category) AND " +
            "(:status IS NULL OR c.status = :status)")
    List<Club> findByFilters(
            @Param("schoolId") Long schoolId,
            @Param("category") ClubCategory category,
            @Param("status") ClubStatus status
    );
}
