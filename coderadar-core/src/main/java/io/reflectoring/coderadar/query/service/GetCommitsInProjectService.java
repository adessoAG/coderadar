package io.reflectoring.coderadar.query.service;

import io.reflectoring.coderadar.projectadministration.ProjectNotFoundException;
import io.reflectoring.coderadar.projectadministration.domain.Commit;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.query.port.driven.GetCommitsInProjectPort;
import io.reflectoring.coderadar.query.port.driver.GetCommitsInProjectUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetCommitsInProjectService implements GetCommitsInProjectUseCase {
  private final GetCommitsInProjectPort getCommitsInProjectPort;
  private final GetProjectPort getProjectPort;

  public GetCommitsInProjectService(
      GetCommitsInProjectPort getCommitsInProjectPort, GetProjectPort getProjectPort) {
    this.getCommitsInProjectPort = getCommitsInProjectPort;
    this.getProjectPort = getProjectPort;
  }

  @Override
  public List<Commit> get(Long projectId) {
    if (getProjectPort.existsById(projectId)) {
      return getCommitsInProjectPort.getCommitsSortedByTimestampDescWithNoRelationships(projectId);
    } else {
      throw new ProjectNotFoundException(projectId);
    }
  }
}
