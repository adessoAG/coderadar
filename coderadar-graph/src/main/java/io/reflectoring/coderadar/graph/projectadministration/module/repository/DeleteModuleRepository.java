package io.reflectoring.coderadar.graph.projectadministration.module.repository;

import io.reflectoring.coderadar.core.projectadministration.domain.Module;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeleteModuleRepository extends Neo4jRepository<Module, Long> {}
