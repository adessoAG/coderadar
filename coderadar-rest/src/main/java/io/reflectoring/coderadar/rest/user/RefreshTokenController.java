package io.reflectoring.coderadar.rest.user;

import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.rest.domain.ErrorMessageResponse;
import io.reflectoring.coderadar.useradministration.AccessTokenNotExpiredException;
import io.reflectoring.coderadar.useradministration.RefreshTokenNotFoundException;
import io.reflectoring.coderadar.useradministration.UserNotFoundException;
import io.reflectoring.coderadar.useradministration.port.driver.refresh.RefreshTokenCommand;
import io.reflectoring.coderadar.useradministration.port.driver.refresh.RefreshTokenResponse;
import io.reflectoring.coderadar.useradministration.port.driver.refresh.RefreshTokenUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class RefreshTokenController implements AbstractBaseController {
  private final RefreshTokenUseCase refreshTokenUseCase;

  public RefreshTokenController(RefreshTokenUseCase refreshTokenUseCase) {
    this.refreshTokenUseCase = refreshTokenUseCase;
  }

  @PostMapping(
      path = "/user/refresh",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity refreshToken(@RequestBody @Validated RefreshTokenCommand command) {
    try {
      return new ResponseEntity<>(
          new RefreshTokenResponse(refreshTokenUseCase.refreshToken(command)), HttpStatus.OK);
    } catch (RefreshTokenNotFoundException | UserNotFoundException e) {
      return new ResponseEntity<>(
          new ErrorMessageResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    } catch (AccessTokenNotExpiredException e) {
      return new ResponseEntity<>(new ErrorMessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
  }
}
