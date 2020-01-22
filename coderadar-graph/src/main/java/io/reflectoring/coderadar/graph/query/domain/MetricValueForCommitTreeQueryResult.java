package io.reflectoring.coderadar.graph.query.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;
import java.util.Map;

@QueryResult
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class MetricValueForCommitTreeQueryResult {
  private String path;
  private List<Map<String, Object>> metrics;
}
