package io.reflectoring.coderadar.analyzer.service;

import io.reflectoring.coderadar.CoderadarConfigurationProperties;
import io.reflectoring.coderadar.analyzer.domain.MetricValue;
import io.reflectoring.coderadar.plugin.api.FileMetrics;
import io.reflectoring.coderadar.plugin.api.Metric;
import io.reflectoring.coderadar.plugin.api.SourceCodeFileAnalyzerPlugin;
import io.reflectoring.coderadar.projectadministration.domain.Commit;
import io.reflectoring.coderadar.projectadministration.domain.File;
import io.reflectoring.coderadar.projectadministration.domain.Project;
import io.reflectoring.coderadar.vcs.UnableToGetCommitContentException;
import io.reflectoring.coderadar.vcs.port.driven.GetRawCommitContentPort;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Performs analysis on a commit. */
@Service
@RequiredArgsConstructor
public class AnalyzeCommitService {

  private final AnalyzeFileService analyzeFileService;
  private final GetRawCommitContentPort getRawCommitContentPort;
  private final CoderadarConfigurationProperties coderadarConfigurationProperties;

  /**
   * Analyzes a single commit.
   *
   * @param commit The commit to analyze.
   * @param project The project the commit is part of.
   * @param analyzers The analyzers to use.
   * @return A list of metric values for the given commit.
   */
  public List<MetricValue> analyzeCommit(
      Commit commit, Project project, List<SourceCodeFileAnalyzerPlugin> analyzers) {
    List<MetricValue> metricValues = new ArrayList<>();
    analyzeBulk(commit.getHash(), commit.getChangedFiles(), analyzers, project)
        .forEach(
            (fileId, fileMetrics) ->
                metricValues.addAll(getMetrics(fileMetrics, commit.getId(), fileId)));
    return metricValues;
  }

  /**
   * Analyzes all files of a commit in bulk.
   *
   * @param commitHash The commit hash.
   * @param files The files of the commit.
   * @param analyzers The analyzers to use.
   * @param project The project the commit is in.
   * @return A map of File and corresponding FileMetrics
   */
  private HashMap<Long, FileMetrics> analyzeBulk(
      String commitHash,
      List<File> files,
      List<SourceCodeFileAnalyzerPlugin> analyzers,
      Project project) {
    HashMap<Long, FileMetrics> fileMetricsMap = new LinkedHashMap<>();
    try {
      HashMap<File, byte[]> fileContents =
          getRawCommitContentPort.getCommitContentBulkWithFiles(
              coderadarConfigurationProperties.getWorkdir()
                  + "/projects/"
                  + project.getWorkdirName(),
              files,
              commitHash);
      fileContents.forEach(
          (file, content) ->
              fileMetricsMap.put(
                  file.getId(),
                  analyzeFileService.analyzeFile(analyzers, file.getPath(), content)));
    } catch (UnableToGetCommitContentException e) {
      e.printStackTrace();
    }
    return fileMetricsMap;
  }

  /**
   * Extracts the metrics out of the FileMetrics (plugin) objects and returns a list of MetricValues
   *
   * @param fileMetrics The file metrics to use.
   * @param commitId The DB id of the commit.
   * @param fileId The DB id of the current file.
   * @return A list of MetricValues.
   */
  private List<MetricValue> getMetrics(FileMetrics fileMetrics, Long commitId, Long fileId) {
    List<MetricValue> metricValues = new ArrayList<>(fileMetrics.getMetrics().size());
    for (Metric metric : fileMetrics.getMetrics()) {
      metricValues.add(
          new MetricValue(
              metric.getId(),
              fileMetrics.getMetricCount(metric),
              commitId,
              fileId,
              fileMetrics.getFindings(metric)));
    }
    return metricValues;
  }
}
