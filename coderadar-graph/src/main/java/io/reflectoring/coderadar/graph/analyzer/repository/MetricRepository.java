package io.reflectoring.coderadar.graph.analyzer.repository;

import io.reflectoring.coderadar.graph.analyzer.domain.FileIdAndMetricQueryResult;
import io.reflectoring.coderadar.graph.analyzer.domain.MetricValueEntity;
import java.util.HashMap;
import java.util.List;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository extends Neo4jRepository<MetricValueEntity, Long> {

  /**
   * NOTE: only used in tests. WILL cause an out of memory exception if used on a sufficiently large
   * project.
   *
   * @param projectId The project id.
   * @return All of the metric values in a project.
   */
  @Query("MATCH (p)-[:CONTAINS*]->()-[:MEASURED_BY]->(m) WHERE ID(p) = {0} RETURN m")
  List<MetricValueEntity> findByProjectId(long projectId);

  /**
   * Creates [:MEASURED_BY] relationships between metric values and files and [:VALID_FOR]
   * relationships between metric values and commits.
   *
   * @param commitAndFileRels A list of maps, each containing the id of an existing
   *     MetricValueEntity ("metricId"), an existing FileEntity ("fileId") and an existing
   *     CommitEntity ("commitId").
   */
  @Query(
      "UNWIND {0} as x "
          + "MATCH (m) WHERE ID(m) = x.metricId "
          + "MATCH (f) WHERE ID(f) = x.fileId "
          + "MATCH (c) WHERE ID(c) = x.commitId "
          + "CREATE (f)-[:MEASURED_BY]->(m)-[:VALID_FOR]->(c)")
  void createFileAndCommitRelationships(List<HashMap<String, Object>> commitAndFileRels);

  /**
   * Uses APOC.
   *
   * @param projectId The project id.
   * @param branchName The branch name.
   * @return All files and their corresponding metrics for the head commit of the given branch
   */
  @Query(
      "MATCH (p)-[:HAS_BRANCH]->(b:BranchEntity)-[:POINTS_TO]->(c) WHERE ID(p) = {0} AND b.name = {1} WITH c LIMIT 1 "
          + "CALL apoc.path.subgraphNodes(c, {relationshipFilter:'IS_CHILD_OF>'}) YIELD node WITH node as c ORDER BY c.timestamp DESC WITH collect(c) as commits "
          + "CALL apoc.cypher.run('UNWIND commits as c OPTIONAL MATCH (f)<-[:RENAMED_FROM]-()-[:CHANGED_IN]->(c) RETURN collect(f) as renames', {commits: commits}) "
          + "YIELD value WITH commits, value.renames as renames "
          + "CALL apoc.cypher.run('UNWIND commits as c OPTIONAL MATCH (f)-[:CHANGED_IN {changeType: \"DELETE\"}]->(c) "
          + "RETURN collect(f) as deletes', {commits: commits}) YIELD value WITH commits, renames, value.deletes as deletes "
          + "UNWIND commits as c "
          + "MATCH (f)-[:MEASURED_BY]->(m)-[:VALID_FOR]->(c) WHERE "
          + "NOT(f IN deletes OR f IN renames) AND m.value <> 0 WITH ID(f) as id, m.name as name, head(collect(m)) as metric "
          + "RETURN  id, collect(metric) as metrics")
  List<FileIdAndMetricQueryResult> getLastMetricsForFiles(long projectId, String branchName);

  @Query("MATCH (c)<-[:VALID_FOR]-(m) WHERE ID(c) = {0} DETACH DELETE m")
  void deleteMetricsForCommit(long id);
}
