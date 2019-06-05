package io.reflectoring.coderadar.core.projectadministration.domain;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * Contains an Ant-style path pattern to define a certain set of files that is of importance to a
 * coderadar Project.
 */
@NodeEntity
@Data
public class FilePattern {
  private Long id;
  private String pattern;
  private InclusionType inclusionType; // TODO:  A converter may have to be used here.

  @Relationship(direction = Relationship.INCOMING, type = "HAS")
  private Project project;
}
