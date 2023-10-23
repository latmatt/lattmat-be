package solution.com.lattmat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import solution.com.lattmat.model.Users;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findUsersByPhoneNumber(String phoneNumber);
}
