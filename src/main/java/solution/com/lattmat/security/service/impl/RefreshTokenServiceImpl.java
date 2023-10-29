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
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static solution.com.lattmat.security.config.SecurityConfigConst.REFRESH_TOKEN_EXPIRATION_MILLS;

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final RefreshTokenRepo refreshTokenRepository;
  private final UserRepository userRepository;

  @Override
  public Optional<RefreshToken> findRefreshTokenByUserPhoneNumber(String phoneNumber) {
    return refreshTokenRepository.findRefreshTokenByUser_PhoneNumber(phoneNumber);
  }

  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Override
  @Transactional
  public RefreshToken createRefreshToken(String phoneNumber) {

    return refreshTokenRepository.findRefreshTokenByUser_LoginId(phoneNumber).orElseGet(() -> {
      Users user = userRepository.findUsersByLoginId(phoneNumber).get();
      return generateRefreshToken(user);
    });

  }

  @Override
  public RefreshToken verifyExpiration(RefreshToken token) {
    System.out.println(token.getExpiryDate());
    System.out.println(new Date());
    if (token.getExpiryDate().before(new Date())) {
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
      refreshToken.setExpiryDate(Date.from(Instant.now().plus(REFRESH_TOKEN_EXPIRATION_MILLS, ChronoUnit.MILLIS)));
      refreshToken.setToken(UUID.randomUUID().toString());

      refreshToken = refreshTokenRepository.save(refreshToken);
    }catch (DataIntegrityViolationException e){
      throw e;
    }

    return refreshToken;
  }
}
