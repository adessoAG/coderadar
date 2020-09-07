package io.reflectoring.coderadar.useradministration.service.teams;

import io.reflectoring.coderadar.projectadministration.ProjectNotFoundException;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.useradministration.TeamNotFoundException;
import io.reflectoring.coderadar.useradministration.domain.ProjectRole;
import io.reflectoring.coderadar.useradministration.port.driven.AddTeamToProjectPort;
import io.reflectoring.coderadar.useradministration.port.driven.GetTeamPort;
import io.reflectoring.coderadar.useradministration.port.driver.teams.AddTeamToProjectUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddTeamToProjectService implements AddTeamToProjectUseCase {

  private final GetProjectPort getProjectPort;
  private final AddTeamToProjectPort addTeamToProjectPort;
  private final GetTeamPort getTeamPort;

  @Override
  public void addTeamToProject(long projectId, long teamId, ProjectRole role) {
    if (!getProjectPort.existsById(projectId)) {
      throw new ProjectNotFoundException(projectId);
    }
    if (!getTeamPort.existsById(teamId)) {
      throw new TeamNotFoundException(teamId);
    }
    addTeamToProjectPort.addTeamToProject(projectId, teamId, role);
  }
}
