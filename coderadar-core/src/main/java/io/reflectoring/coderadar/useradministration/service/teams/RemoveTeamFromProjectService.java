package io.reflectoring.coderadar.useradministration.service.teams;

import io.reflectoring.coderadar.projectadministration.ProjectNotFoundException;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.useradministration.TeamNotAssignedException;
import io.reflectoring.coderadar.useradministration.TeamNotFoundException;
import io.reflectoring.coderadar.useradministration.port.driven.GetTeamPort;
import io.reflectoring.coderadar.useradministration.port.driven.ListTeamsForProjectPort;
import io.reflectoring.coderadar.useradministration.port.driven.RemoveTeamFromProjectPort;
import io.reflectoring.coderadar.useradministration.port.driver.teams.RemoveTeamFromProjectUseCase;
import org.springframework.stereotype.Service;

@Service
public class RemoveTeamFromProjectService implements RemoveTeamFromProjectUseCase {

  private final GetTeamPort getTeamPort;
  private final GetProjectPort getProjectPort;
  private final RemoveTeamFromProjectPort removeTeamFromProjectPort;
  private final ListTeamsForProjectPort listTeamsForProjectPort;

  public RemoveTeamFromProjectService(
      GetTeamPort getTeamPort,
      GetProjectPort getProjectPort,
      RemoveTeamFromProjectPort removeTeamFromProjectPort,
      ListTeamsForProjectPort listTeamsForProjectPort) {
    this.getTeamPort = getTeamPort;
    this.getProjectPort = getProjectPort;
    this.removeTeamFromProjectPort = removeTeamFromProjectPort;
    this.listTeamsForProjectPort = listTeamsForProjectPort;
  }

  @Override
  public void removeTeam(long projectId, long teamId) {
    if (!getProjectPort.existsById(projectId)) {
      throw new ProjectNotFoundException(projectId);
    }
    if (!getTeamPort.existsById(teamId)) {
      throw new TeamNotFoundException(teamId);
    }
    if (listTeamsForProjectPort.listTeamsForProject(projectId).stream()
        .anyMatch(team -> team.getId() == teamId)) {
      removeTeamFromProjectPort.removeTeam(projectId, teamId);
    } else {
      throw new TeamNotAssignedException(projectId, teamId);
    }
  }
}
