package io.reflectoring.coderadar.projectadministration.service.project;

import io.reflectoring.coderadar.projectadministration.ProjectNotFoundException;
import io.reflectoring.coderadar.projectadministration.domain.Project;
import io.reflectoring.coderadar.projectadministration.domain.ProjectWithRoles;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.projectadministration.port.driver.project.get.GetProjectUseCase;
import io.reflectoring.coderadar.useradministration.UserNotFoundException;
import io.reflectoring.coderadar.useradministration.port.driven.GetUserPort;
import org.springframework.stereotype.Service;

@Service
public class GetProjectService implements GetProjectUseCase {

  private final GetProjectPort getProjectPort;
  private final GetUserPort getUserPort;

  public GetProjectService(GetProjectPort getProjectPort, GetUserPort getUserPort) {
    this.getProjectPort = getProjectPort;
    this.getUserPort = getUserPort;
  }

  @Override
  public Project get(long id) {
    return getProjectPort.get(id);
  }

  @Override
  public ProjectWithRoles getWithRoles(long projectId, long userId) {
    if (getUserPort.existsById(userId)) {
      if (getProjectPort.existsById(projectId)) {
        return getProjectPort.getWithRoles(projectId, userId);
      } else {
        throw new ProjectNotFoundException(projectId);
      }
    } else {
      throw new UserNotFoundException(userId);
    }
  }
}
