package io.reflectoring.coderadar.rest.useradministration.teams;

import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.rest.domain.GetTeamResponse;
import io.reflectoring.coderadar.useradministration.domain.Team;
import io.reflectoring.coderadar.useradministration.port.driver.teams.get.GetTeamUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static io.reflectoring.coderadar.rest.GetTeamResponseMapper.mapTeam;
import static io.reflectoring.coderadar.rest.GetUserResponseMapper.mapUsers;

@RestController
@Transactional
public class GetTeamController implements AbstractBaseController {
    private final GetTeamUseCase getTeamUseCase;

    public GetTeamController(GetTeamUseCase getTeamUseCase) {
        this.getTeamUseCase = getTeamUseCase;
    }

    @GetMapping(path = "/teams/{teamId}")
    public ResponseEntity<GetTeamResponse> getTeam(@PathVariable long teamId) {
        Team team = getTeamUseCase.get(teamId);
        return new ResponseEntity<>(mapTeam(team), HttpStatus.OK);
    }
}
