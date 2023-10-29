package solution.com.lattmat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import solution.com.lattmat.entity.Role;
import solution.com.lattmat.enumeration.UserRoleType;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findRoleByRoleType(UserRoleType userRoleType);
}
