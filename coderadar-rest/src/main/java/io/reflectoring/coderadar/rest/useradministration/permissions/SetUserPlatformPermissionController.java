package io.reflectoring.coderadar.rest.useradministration.permissions;

import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.useradministration.port.driver.permissions.SetUserPlatformPermissionUseCase;
import javax.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequiredArgsConstructor
public class SetUserPlatformPermissionController implements AbstractBaseController {

  private final SetUserPlatformPermissionUseCase setUserPlatformPermissionUseCase;

  @PostMapping("/users/{userId}/admin")
  public ResponseEntity<HttpStatus> setUserPlatformPermission(
      @PathVariable("userId") long userId, @PathParam("isAdmin") boolean isAdmin) {
    setUserPlatformPermissionUseCase.setUserPlatformPermission(userId, isAdmin);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
