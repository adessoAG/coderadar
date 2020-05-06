package io.reflectoring.coderadar.useradministration.service.teams.get;

import io.reflectoring.coderadar.useradministration.domain.Team;
import io.reflectoring.coderadar.useradministration.port.driver.teams.get.ListTeamsUseCase;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ListTeamsService implements ListTeamsUseCase {

  @Override
  public List<Team> listTeams() {
    return null;
  }
}
