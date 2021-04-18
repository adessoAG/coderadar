package io.reflectoring.coderadar.graph.projectadministration.domain;

import io.reflectoring.coderadar.domain.Module;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/** @see Module */
@NodeEntity
@Data
@EqualsAndHashCode
public class ModuleEntity {
  private Long id;
  private String path;

  @Relationship(direction = Relationship.INCOMING, type = "CONTAINS")
  @EqualsAndHashCode.Exclude
  private ProjectEntity project;

  @Relationship(direction = Relationship.INCOMING, type = "CONTAINS")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private ModuleEntity parentModule;

  @Relationship(type = "CONTAINS")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<ModuleEntity> childModules = new ArrayList<>();

  @Relationship(type = "CONTAINS")
  @ToString.Exclude
  private List<FileEntity> files = new ArrayList<>();
}
