package io.reflectoring.coderadar.rest.useradministration.teams;

import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.rest.JsonListWrapper;
import io.reflectoring.coderadar.rest.domain.ErrorMessageResponse;
import io.reflectoring.coderadar.useradministration.UserNotInTeamException;
import io.reflectoring.coderadar.useradministration.port.driver.teams.RemoveUsersFromTeamUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class RemoveUsersFromTeamController implements AbstractBaseController {
  private final RemoveUsersFromTeamUseCase removeUsersFromTeamUseCase;

  public RemoveUsersFromTeamController(RemoveUsersFromTeamUseCase removeUsersFromTeamUseCase) {
    this.removeUsersFromTeamUseCase = removeUsersFromTeamUseCase;
  }

  @DeleteMapping(path = "/teams/{teamId}/users", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ErrorMessageResponse> removeUsersFromTeam(
      @PathVariable long teamId, @RequestBody JsonListWrapper<Long> userIds) {
    try {
      removeUsersFromTeamUseCase.removeUsersFromTeam(teamId, userIds.getElements());
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (UserNotInTeamException e) {
      return new ResponseEntity<>(new ErrorMessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
  }
}
