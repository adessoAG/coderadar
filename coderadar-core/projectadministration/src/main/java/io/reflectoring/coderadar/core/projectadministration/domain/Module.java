package io.reflectoring.coderadar.core.projectadministration.domain;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * The codebase may be organized into modules, each module starting at a certain path. All files
 * within that path are considered to be part of the module.
 */
@NodeEntity
@Data
public class Module {
  private Long id;
  private String path;

  @Relationship(direction = INCOMING, type = "HAS")
  private Project project;
}
