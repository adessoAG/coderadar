package io.reflectoring.coderadar.useradministration.service.refresh;

import io.reflectoring.coderadar.domain.RefreshToken;
import io.reflectoring.coderadar.domain.User;
import io.reflectoring.coderadar.useradministration.AccessTokenNotExpiredException;
import io.reflectoring.coderadar.useradministration.RefreshTokenNotFoundException;
import io.reflectoring.coderadar.useradministration.UserNotFoundException;
import io.reflectoring.coderadar.useradministration.port.driven.GetUserPort;
import io.reflectoring.coderadar.useradministration.port.driven.RefreshTokenPort;
import io.reflectoring.coderadar.useradministration.port.driver.refresh.RefreshTokenCommand;
import io.reflectoring.coderadar.useradministration.port.driver.refresh.RefreshTokenUseCase;
import io.reflectoring.coderadar.useradministration.service.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

  private final GetUserPort getUserPort;
  private final RefreshTokenPort refreshTokenPort;
  private final TokenService tokenService;

  @Override
  public String refreshToken(RefreshTokenCommand command) {
    if (tokenService.isExpired(command.getAccessToken())) {
      return createAccessToken(command.getRefreshToken());
    } else {
      throw new AccessTokenNotExpiredException();
    }
  }

  /**
   * Creates new access token, if the refresh token and the user having token are valid.
   *
   * @param refreshToken the refresh token to be checked
   * @return access token
   */
  public String createAccessToken(String refreshToken) {
    User user = checkUser(refreshToken);
    String username = tokenService.getUsername(refreshToken);
    if (user != null) {
      return tokenService.generateAccessToken(user.getId(), user.getUsername());
    } else {
      throw new UserNotFoundException(username);
    }
  }

  /**
   * Checks the validity of refresh token and validity of the user, who sent the token.
   *
   * @param refreshToken the refresh token of the user to check
   * @throws RefreshTokenNotFoundException if the token was not found.
   * @throws UsernameNotFoundException if the user from the refresh token was not found.
   * @return User with the given refresh token.
   */
  public User checkUser(String refreshToken) {
    RefreshToken refreshTokenEntity = refreshTokenPort.findByToken(refreshToken);
    if (refreshTokenEntity == null) {
      throw new RefreshTokenNotFoundException();
    }
    User user = getUser(refreshToken);
    tokenService.verify(refreshToken);
    return user;
  }

  /**
   * reads the username from the refresh token and loads the user by thе username from thе data
   * base.
   *
   * @param refreshToken refresh token
   * @return User according to the refresh token
   */
  public User getUser(String refreshToken) {
    String username = tokenService.getUsername(refreshToken);
    return getUserPort.getUserByUsername(username);
  }
}
