package io.reflectoring.coderadar.query.port.driver.metrictree;

import io.reflectoring.coderadar.query.domain.MetricTree;

public interface GetMetricTreeForCommitUseCase {

  /**
   * @param projectId The id of the project.
   * @param command The command containing the commit hash and the metrics we are interested in.
   * @return A tree structure containing each file in the commit along with metric values.
   */
  MetricTree get(long projectId, GetMetricTreeForCommitCommand command);
}
