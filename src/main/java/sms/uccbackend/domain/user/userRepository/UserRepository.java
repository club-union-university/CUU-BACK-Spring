package sms.uccbackend.domain.user.userRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import sms.uccbackend.domain.user.userEntity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirebaseUid(String firebaseUid);

    Optional<User> findByEmail(String email);

    boolean existsByFirebaseUid(String firebaseUid);

    boolean existsByNickname(String nickname);
}
