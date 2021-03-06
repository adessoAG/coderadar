package io.reflectoring.coderadar.projectadministration.service.analyzerconfig;

import io.reflectoring.coderadar.analyzer.domain.AnalyzerConfiguration;
import io.reflectoring.coderadar.projectadministration.port.driven.analyzerconfig.DeleteAnalyzerConfigurationPort;
import io.reflectoring.coderadar.projectadministration.port.driven.analyzerconfig.GetAnalyzerConfigurationPort;
import io.reflectoring.coderadar.projectadministration.port.driver.analyzerconfig.delete.DeleteAnalyzerConfigurationUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteAnalyzerConfigurationService implements DeleteAnalyzerConfigurationUseCase {

  private final DeleteAnalyzerConfigurationPort port;
  private final GetAnalyzerConfigurationPort getAnalyzerConfigurationPort;
  private static final Logger logger =
      LoggerFactory.getLogger(DeleteAnalyzerConfigurationService.class);

  @Override
  public void deleteAnalyzerConfiguration(long id, long projectId) {
    AnalyzerConfiguration analyzerConfiguration =
        getAnalyzerConfigurationPort.getAnalyzerConfiguration(id);
    port.deleteAnalyzerConfiguration(id);
    logger.info(
        "Deleted analyzerConfiguration {} for project with id {}",
        analyzerConfiguration.getAnalyzerName(),
        projectId);
  }
}
