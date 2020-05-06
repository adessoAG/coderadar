package io.reflectoring.coderadar.rest.useradministration.permissions;

import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.useradministration.domain.ProjectRole;
import io.reflectoring.coderadar.useradministration.port.driver.permissions.SetUserRoleForProjectUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class SetUserRoleForProjectController implements AbstractBaseController {
  private final SetUserRoleForProjectUseCase setUserRoleForProjectUseCase;

  public SetUserRoleForProjectController(
      SetUserRoleForProjectUseCase setUserRoleForProjectUseCase) {
    this.setUserRoleForProjectUseCase = setUserRoleForProjectUseCase;
  }

  @PostMapping(
      path = "/projects/{projectId}/users/{userId}",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatus> setUserRoleForProject(
      @PathVariable long projectId, @PathVariable long userId, @RequestBody ProjectRole role) {
    setUserRoleForProjectUseCase.setRole(projectId, userId, role);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
