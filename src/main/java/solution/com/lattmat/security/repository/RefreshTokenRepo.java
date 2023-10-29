package solution.com.lattmat.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import solution.com.lattmat.model.Users;
import solution.com.lattmat.security.entity.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findRefreshTokenByUser_PhoneNumber(String phoneNumber);
    Optional<RefreshToken> findRefreshTokenByUser_LoginId(String loginId);
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(Users user);
}
