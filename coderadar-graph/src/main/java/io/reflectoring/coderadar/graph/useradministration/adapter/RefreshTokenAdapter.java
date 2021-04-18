package io.reflectoring.coderadar.graph.useradministration.adapter;

import io.reflectoring.coderadar.domain.RefreshToken;
import io.reflectoring.coderadar.domain.User;
import io.reflectoring.coderadar.graph.useradministration.RefreshTokenMapper;
import io.reflectoring.coderadar.graph.useradministration.domain.RefreshTokenEntity;
import io.reflectoring.coderadar.graph.useradministration.repository.RefreshTokenRepository;
import io.reflectoring.coderadar.graph.useradministration.repository.UserRepository;
import io.reflectoring.coderadar.useradministration.RefreshTokenNotFoundException;
import io.reflectoring.coderadar.useradministration.port.driven.RefreshTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenAdapter implements RefreshTokenPort {

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;
  private final RefreshTokenMapper refreshTokenMapper = new RefreshTokenMapper();

  @Override
  public RefreshToken findByToken(String refreshToken) {
    RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken);
    if (refreshTokenEntity != null) {
      refreshTokenEntity.setUser(userRepository.findUserByRefreshToken(refreshToken));
      return refreshTokenMapper.mapGraphObject(refreshTokenEntity);
    } else {
      throw new RefreshTokenNotFoundException();
    }
  }

  @Override
  public void deleteByUser(User user) {
    refreshTokenRepository.deleteByUser((user.getId()));
  }

  @Override
  public void saveToken(RefreshToken refreshToken, long userId) {
    refreshTokenRepository.saveToken(refreshToken.getToken(), userId);
  }
}
