package io.reflectoring.coderadar.rest.useradministration;

import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.rest.domain.ErrorMessageResponse;
import io.reflectoring.coderadar.useradministration.port.driver.login.LoginUserCommand;
import io.reflectoring.coderadar.useradministration.port.driver.login.LoginUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
public class LoginUserController implements AbstractBaseController {
  private final LoginUserUseCase loginUserUseCase;

  @PostMapping(
      path = "/user/auth",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> login(@RequestBody @Validated LoginUserCommand command) {
    try {
      return new ResponseEntity<>(loginUserUseCase.login(command), HttpStatus.OK);
    } catch (AuthenticationException e) {
      return new ResponseEntity<>(new ErrorMessageResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }
  }
}
