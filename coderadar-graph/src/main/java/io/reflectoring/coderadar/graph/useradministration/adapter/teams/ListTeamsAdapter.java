package io.reflectoring.coderadar.graph.useradministration.adapter.teams;

import io.reflectoring.coderadar.graph.useradministration.TeamMapper;
import io.reflectoring.coderadar.graph.useradministration.repository.TeamRepository;
import io.reflectoring.coderadar.useradministration.domain.Team;
import io.reflectoring.coderadar.useradministration.port.driven.ListTeamsPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListTeamsAdapter implements ListTeamsPort {

  private final TeamRepository teamRepository;
  private final TeamMapper mapper = new TeamMapper();

  @Override
  public List<Team> listTeams() {
    return mapper.mapNodeEntities(teamRepository.findAllWithMembers());
  }
}
