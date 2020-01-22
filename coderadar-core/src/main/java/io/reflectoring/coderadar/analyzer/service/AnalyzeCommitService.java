package io.reflectoring.coderadar.analyzer.service;

import io.reflectoring.coderadar.CoderadarConfigurationProperties;
import io.reflectoring.coderadar.analyzer.domain.Finding;
import io.reflectoring.coderadar.analyzer.domain.MetricValue;
import io.reflectoring.coderadar.analyzer.port.driver.AnalyzeCommitUseCase;
import io.reflectoring.coderadar.plugin.api.FileMetrics;
import io.reflectoring.coderadar.plugin.api.Metric;
import io.reflectoring.coderadar.plugin.api.SourceCodeFileAnalyzerPlugin;
import io.reflectoring.coderadar.projectadministration.domain.Commit;
import io.reflectoring.coderadar.projectadministration.domain.File;
import io.reflectoring.coderadar.projectadministration.domain.FileToCommitRelationship;
import io.reflectoring.coderadar.projectadministration.domain.Project;
import io.reflectoring.coderadar.vcs.UnableToGetCommitContentException;
import io.reflectoring.coderadar.vcs.port.driver.GetCommitRawContentUseCase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/** Performs analysis on a commit. */
@Service
public class AnalyzeCommitService implements AnalyzeCommitUseCase {

  private final AnalyzeFileService analyzeFileService;
  private final GetCommitRawContentUseCase getCommitRawContentUseCase;
  private final CoderadarConfigurationProperties coderadarConfigurationProperties;

  public AnalyzeCommitService(
      AnalyzeFileService analyzeFileService,
      GetCommitRawContentUseCase getCommitRawContentUseCase,
      CoderadarConfigurationProperties coderadarConfigurationProperties) {
    this.analyzeFileService = analyzeFileService;
    this.getCommitRawContentUseCase = getCommitRawContentUseCase;
    this.coderadarConfigurationProperties = coderadarConfigurationProperties;
  }

  /**
   * Analyzes a single commit.
   *
   * @param commit The commit to analyze.
   * @param project The project the commit is part of.
   * @param analyzers The analyzers to use.
   * @return A list of metric values for the given commit.
   */
  @Override
  public List<MetricValue> analyzeCommit(
      Commit commit, Project project, List<SourceCodeFileAnalyzerPlugin> analyzers) {
    List<MetricValue> metricValues = new ArrayList<>(400);
    List<File> files =
        commit.getTouchedFiles().stream()
            .map(FileToCommitRelationship::getFile)
            .collect(Collectors.toList());
    analyzeBulk(commit, files, analyzers, project)
        .forEach(
            (file, fileMetrics) ->
                metricValues.addAll(getMetrics(fileMetrics, commit, file.getId())));
    return metricValues;
  }

  /**
   * Analyzes all files of a commit in bulk.
   *
   * @param commit The commit to analyze.
   * @param files The files of the commit.
   * @param analyzers The analyzers to use.
   * @param project The project the commit is in.
   * @return A map of File and corresponding FileMetrics
   */
  private HashMap<File, FileMetrics> analyzeBulk(
      Commit commit,
      List<File> files,
      List<SourceCodeFileAnalyzerPlugin> analyzers,
      Project project) {
    HashMap<File, FileMetrics> fileMetricsMap = new LinkedHashMap<>();
    try {
      HashMap<File, byte[]> fileContents =
          getCommitRawContentUseCase.getCommitContentBulkWithFiles(
              coderadarConfigurationProperties.getWorkdir()
                  + "/projects/"
                  + project.getWorkdirName(),
              files,
              commit.getName());
      fileContents.forEach(
          (key, value) ->
              fileMetricsMap.put(
                  key, analyzeFileService.analyzeFile(analyzers, key.getPath(), value)));
    } catch (UnableToGetCommitContentException e) {
      e.printStackTrace();
    }
    return fileMetricsMap;
  }

  /**
   * Extracts the metrics out of the FileMetrics (plugin) objects and returns a list of MetricValues
   *
   * @param fileMetrics The file metrics to use.
   * @param commit The commit.
   * @param fileId The DB id of the current file.
   * @return A list of MetricValues.
   */
  private List<MetricValue> getMetrics(FileMetrics fileMetrics, Commit commit, Long fileId) {
    List<MetricValue> metricValues = new ArrayList<>();
    for (Metric metric : fileMetrics.getMetrics()) {
      List<Finding> findings =
          fileMetrics.getFindings(metric).stream()
              .map(
                  finding ->
                      new Finding(
                          finding.getLineStart(),
                          finding.getLineEnd(),
                          finding.getCharStart(),
                          finding.getCharEnd()))
              .collect(Collectors.toList());
      MetricValue metricValue =
          new MetricValue(
              metric.getId(), fileMetrics.getMetricCount(metric), commit, findings, fileId);
      metricValues.add(metricValue);
    }
    return metricValues;
  }
}
