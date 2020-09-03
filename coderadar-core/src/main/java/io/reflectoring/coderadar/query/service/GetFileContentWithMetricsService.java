package io.reflectoring.coderadar.query.service;

import io.reflectoring.coderadar.CoderadarConfigurationProperties;
import io.reflectoring.coderadar.ValidationUtils;
import io.reflectoring.coderadar.projectadministration.domain.Project;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.query.domain.FileContentWithMetrics;
import io.reflectoring.coderadar.query.domain.MetricWithFindings;
import io.reflectoring.coderadar.query.port.driven.GetMetricsAndFindingsForFilePort;
import io.reflectoring.coderadar.query.port.driver.filecontent.GetFileContentWithMetricsCommand;
import io.reflectoring.coderadar.query.port.driver.filecontent.GetFileContentWithMetricsUseCase;
import io.reflectoring.coderadar.vcs.port.driven.GetRawCommitContentPort;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetFileContentWithMetricsService implements GetFileContentWithMetricsUseCase {

  private final GetProjectPort getProjectPort;
  private final GetRawCommitContentPort getRawCommitContentPort;
  private final GetMetricsAndFindingsForFilePort getMetricsAndFindingsForFilePort;
  private final CoderadarConfigurationProperties coderadarConfigurationProperties;

  public GetFileContentWithMetricsService(
      GetProjectPort getProjectPort,
      GetRawCommitContentPort getRawCommitContentPort,
      GetMetricsAndFindingsForFilePort getMetricsAndFindingsForFilePort,
      CoderadarConfigurationProperties coderadarConfigurationProperties) {
    this.getProjectPort = getProjectPort;
    this.getRawCommitContentPort = getRawCommitContentPort;
    this.getMetricsAndFindingsForFilePort = getMetricsAndFindingsForFilePort;
    this.coderadarConfigurationProperties = coderadarConfigurationProperties;
  }

  @Override
  public FileContentWithMetrics getFileContentWithMetrics(
      long projectId, GetFileContentWithMetricsCommand command) {
    String commitHash = ValidationUtils.validateAndTrimCommitHash(command.getCommitHash());
    Project project = getProjectPort.get(projectId);
    String workdir =
        coderadarConfigurationProperties.getWorkdir() + "/projects/" + project.getWorkdirName();
    byte[] rawContent =
        getRawCommitContentPort.getCommitContent(workdir, command.getFilepath(), commitHash);
    if (rawContent != null) {
      List<MetricWithFindings> metrics =
          getMetricsAndFindingsForFilePort.getMetricsAndFindingsForFile(
              projectId, commitHash, command.getFilepath());
      return new FileContentWithMetrics(new String(rawContent), metrics);
    } else {
      return new FileContentWithMetrics("", null);
    }
  }
}
