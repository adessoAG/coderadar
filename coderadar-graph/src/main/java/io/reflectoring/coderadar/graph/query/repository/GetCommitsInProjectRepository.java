package io.reflectoring.coderadar.graph.query.repository;

import io.reflectoring.coderadar.graph.analyzer.domain.CommitEntity;
import java.util.List;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetCommitsInProjectRepository extends Neo4jRepository<CommitEntity, Long> {

  @Query(
      "MATCH (p1:ProjectEntity)-[:CONTAINS*]-(f1:FileEntity)-[:CHANGED_IN]->(c1:CommitEntity)-[:IS_CHILD_OF*0..2]-(c2:CommitEntity) "
          + "WHERE ID(p1) = {0} "
          + "UNWIND [c1, c2] AS c "
          + "RETURN DISTINCT c "
          + "ORDER BY c.timestamp DESC")
  List<CommitEntity> findByProjectId(Long projectId);

  @Query(
      "MATCH (p:ProjectEntity)-[:CONTAINS*]-(:FileEntity)-[:CHANGED_IN]->(c:CommitEntity) WHERE c.name = {0} AND ID(p) = {1} RETURN c")
  CommitEntity findByNameAndProjectId(String commit, Long projectId);
}