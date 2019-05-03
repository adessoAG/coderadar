package io.reflectoring.coderadar.core.projectadministration.service.module;

import io.reflectoring.coderadar.core.projectadministration.domain.Module;
import io.reflectoring.coderadar.core.projectadministration.port.driven.module.CreateModulePort;
import io.reflectoring.coderadar.core.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.core.projectadministration.port.driver.module.create.CreateModuleCommand;
import io.reflectoring.coderadar.core.projectadministration.port.driver.module.create.CreateModuleUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateModuleService implements CreateModuleUseCase {

  private final GetProjectPort getProjectPort;
  private final CreateModulePort createModulePort;

  @Autowired
  public CreateModuleService(CreateModulePort createModulePort, GetProjectPort getProjectPort) {
    this.getProjectPort = getProjectPort;
    this.createModulePort = createModulePort;
  }

  @Override
  public Long createModule(CreateModuleCommand command, Long projectId) {
    Module module = new Module();
    module.setProject(getProjectPort.get(projectId));
    module.setPath(command.getPath());
    return createModulePort.createModule(module);
  }
}
