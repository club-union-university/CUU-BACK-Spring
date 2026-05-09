package sms.uccbackend.domain.school.schoolRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import sms.uccbackend.domain.school.schoolEntity.Region;
import sms.uccbackend.domain.school.schoolEntity.School;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School, Long> {

    List<School> findByIsWhitelisted(boolean isWhitelisted);

    List<School> findByRegion(Region region);

    boolean existsByName(String name);
}
