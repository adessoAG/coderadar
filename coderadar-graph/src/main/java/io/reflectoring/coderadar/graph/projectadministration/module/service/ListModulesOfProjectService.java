package io.reflectoring.coderadar.graph.projectadministration.module.service;

import io.reflectoring.coderadar.core.projectadministration.domain.Module;
import io.reflectoring.coderadar.core.projectadministration.port.driven.module.ListModulesOfProjectPort;
import io.reflectoring.coderadar.graph.exception.InvalidArgumentException;
import io.reflectoring.coderadar.graph.exception.ProjectNotFoundException;
import io.reflectoring.coderadar.graph.projectadministration.module.repository.ListModulesOfProjectRepository;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.GetProjectRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListModulesOfProjectService implements ListModulesOfProjectPort {
  private final GetProjectRepository getProjectRepository;
  private final ListModulesOfProjectRepository listModulesOfProjectRepository;

  @Autowired
  public ListModulesOfProjectService(
      GetProjectRepository getProjectRepository,
      ListModulesOfProjectRepository listModulesOfProjectRepository) {
    this.getProjectRepository = getProjectRepository;
    this.listModulesOfProjectRepository = listModulesOfProjectRepository;
  }

  @Override
  public List<Module> listModules(Long projectId) {
    if (projectId == null) {
      throw new InvalidArgumentException("When listing modules of project, no null may be passed.");
    }

    if (getProjectRepository.findById(projectId).isPresent()) {
      return listModulesOfProjectRepository.findByProject_Id(projectId);
    } else {
      throw new ProjectNotFoundException("No modules can be listed from a non-existing project.");
    }
  }
}
