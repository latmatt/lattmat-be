package solution.com.lattmat.security.service.impl;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solution.com.lattmat.exception.domain.TokenRefreshException;
import solution.com.lattmat.model.Users;
import solution.com.lattmat.repo.UserRepository;
import solution.com.lattmat.security.config.SecurityConfigConst;
import solution.com.lattmat.security.entity.RefreshToken;
import solution.com.lattmat.security.repository.RefreshTokenRepo;
import solution.com.lattmat.security.service.RefreshTokenService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final RefreshTokenRepo refreshTokenRepository;
  private final UserRepository userRepository;

  @Override
  public Optional<RefreshToken> findRefreshTokenByUserPhoneNumber(String phoneNumber) {
    return refreshTokenRepository.findRefreshTokenByUserPhoneNumber(phoneNumber);
  }

  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Override
  @Transactional
  public RefreshToken createRefreshToken(String phoneNumber) {

    return refreshTokenRepository.findRefreshTokenByUserPhoneNumber(phoneNumber).orElseGet(() -> {
      Users user = userRepository.findUsersByPhoneNumber(phoneNumber).get();
      return generateRefreshToken(user);
    });

  }

  @Override
  @Transactional
  public RefreshToken createOAuthRefreshToken(String loginId) {

    return refreshTokenRepository.findRefreshTokenByUserOauthLoginId(loginId).orElseGet(() -> {
      Users user = userRepository.findUsersByOauthLoginId(loginId).get();
      return generateRefreshToken(user);
    });

  }

  @Override
  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new sign-in request");
    }

    return token;
  }

  @Override
  @Transactional
  public int deleteByUserId(String phoneNumber) {
    return refreshTokenRepository.deleteByUser(userRepository.findUsersByPhoneNumber(phoneNumber).get());
  }

  public RefreshToken generateRefreshToken(Users users) {
    RefreshToken refreshToken = null;
    try{
      refreshToken = new RefreshToken();
      refreshToken.setUser(users);
      refreshToken.setExpiryDate(Instant.now().plusMillis(SecurityConfigConst.EXPIRATION_MILLS));
      refreshToken.setToken(UUID.randomUUID().toString());

      refreshToken = refreshTokenRepository.save(refreshToken);
    }catch (DataIntegrityViolationException e){
      throw e;
    }

    return refreshToken;
  }
}
