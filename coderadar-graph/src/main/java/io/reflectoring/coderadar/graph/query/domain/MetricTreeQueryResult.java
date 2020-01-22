package io.reflectoring.coderadar.graph.query.domain;

import io.reflectoring.coderadar.query.domain.MetricTreeNodeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
class MetricTreeQueryResult {
  private String name;
  private MetricTreeNodeType type;
  private List<MetricValueForCommitQueryResult> metrics;
  private List<MetricTreeQueryResult> children;
}
