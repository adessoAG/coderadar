package io.reflectoring.coderadar.rest.useradministration.teams;

import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.useradministration.port.driver.teams.RemoveTeamFromProjectUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class RemoveTeamFromProjectController implements AbstractBaseController {
  private final RemoveTeamFromProjectUseCase removeTeamFromProjectUseCase;

  public RemoveTeamFromProjectController(
      RemoveTeamFromProjectUseCase removeTeamFromProjectUseCase) {
    this.removeTeamFromProjectUseCase = removeTeamFromProjectUseCase;
  }

  // TODO: which http method should be used here?
  @PostMapping(path = "/project/{projectId}/teams/{teamId}")
  public ResponseEntity<HttpStatus> removeTeamFromProject(
      @PathVariable long projectId, @PathVariable long teamId) {
    removeTeamFromProjectUseCase.removeTeam(teamId); // TODO: change signature of this method
    return new ResponseEntity<>(HttpStatus.OK);
  }
}