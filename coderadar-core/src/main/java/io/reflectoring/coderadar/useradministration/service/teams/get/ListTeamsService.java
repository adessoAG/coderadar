package io.reflectoring.coderadar.useradministration.service.teams.get;

import io.reflectoring.coderadar.useradministration.domain.Team;
import io.reflectoring.coderadar.useradministration.port.driven.ListTeamsPort;
import io.reflectoring.coderadar.useradministration.port.driver.teams.get.ListTeamsUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListTeamsService implements ListTeamsUseCase {

  private final ListTeamsPort listTeamsPort;

  public ListTeamsService(ListTeamsPort listTeamsPort) {
    this.listTeamsPort = listTeamsPort;
  }

  @Override
  public List<Team> listTeams() {
    return listTeamsPort.listTeams();
  }
}
