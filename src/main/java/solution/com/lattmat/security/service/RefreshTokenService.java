package solution.com.lattmat.security.service;

import solution.com.lattmat.security.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findRefreshTokenByUserPhoneNumber(String phoneNumber);
    Optional<RefreshToken> findByToken(String token);
    RefreshToken createRefreshToken(String userId);
    RefreshToken verifyExpiration(RefreshToken token);
    int deleteByUserId(String userId);
}
