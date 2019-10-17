package io.reflectoring.coderadar.projectadministration.service.project;

import io.reflectoring.coderadar.projectadministration.port.driven.project.ListProjectsPort;
import io.reflectoring.coderadar.projectadministration.port.driver.project.get.GetProjectResponse;
import io.reflectoring.coderadar.projectadministration.port.driver.project.get.ListProjectsUseCase;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListProjectsService implements ListProjectsUseCase {

  private final ListProjectsPort listProjectsPort;

  @Autowired
  public ListProjectsService(ListProjectsPort listProjectsPort) {
    this.listProjectsPort = listProjectsPort;
  }

  @Override
  public List<GetProjectResponse> listProjects() {
    return listProjectsPort.getProjectResponses();
  }
}
