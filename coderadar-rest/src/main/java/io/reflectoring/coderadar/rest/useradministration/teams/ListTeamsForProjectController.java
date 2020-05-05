package io.reflectoring.coderadar.rest.useradministration.teams;

import static io.reflectoring.coderadar.rest.GetTeamResponseMapper.mapTeams;

import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.rest.domain.GetTeamResponse;
import io.reflectoring.coderadar.useradministration.domain.Team;
import io.reflectoring.coderadar.useradministration.port.driver.teams.get.ListTeamsForProjectUseCase;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class ListTeamsForProjectController implements AbstractBaseController {
  private final ListTeamsForProjectUseCase listTeamsForProjectUseCase;

  public ListTeamsForProjectController(ListTeamsForProjectUseCase listTeamsForProjectUseCase) {
    this.listTeamsForProjectUseCase = listTeamsForProjectUseCase;
  }

  @GetMapping(path = "/projects/{projectId/teams")
  public ResponseEntity<List<GetTeamResponse>> listTeamsForProject(@PathVariable long projectId) {
    List<Team> teams = listTeamsForProjectUseCase.listTeamsForProject(projectId);
    return new ResponseEntity<>(mapTeams(teams), HttpStatus.OK);
  }
}
