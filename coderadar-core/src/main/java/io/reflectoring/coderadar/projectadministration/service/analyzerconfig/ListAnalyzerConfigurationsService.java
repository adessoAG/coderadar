package io.reflectoring.coderadar.projectadministration.service.analyzerconfig;

import io.reflectoring.coderadar.analyzer.domain.AnalyzerConfiguration;
import io.reflectoring.coderadar.projectadministration.ProjectNotFoundException;
import io.reflectoring.coderadar.projectadministration.port.driven.analyzerconfig.ListAnalyzerConfigurationsPort;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.projectadministration.port.driver.analyzerconfig.get.ListAnalyzerConfigurationsUseCase;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ListAnalyzerConfigurationsService implements ListAnalyzerConfigurationsUseCase {
  private final ListAnalyzerConfigurationsPort port;
  private final GetProjectPort getProjectPort;

  public ListAnalyzerConfigurationsService(
      ListAnalyzerConfigurationsPort port, GetProjectPort getProjectPort) {
    this.port = port;
    this.getProjectPort = getProjectPort;
  }

  @Override
  public List<AnalyzerConfiguration> get(long projectId) {
    if (getProjectPort.existsById(projectId)) {
      return port.listAnalyzerConfigurations(projectId);
    } else {
      throw new ProjectNotFoundException(projectId);
    }
  }
}
