package io.reflectoring.coderadar.graph.projectadministration.filepattern;

import io.reflectoring.coderadar.domain.FilePattern;
import io.reflectoring.coderadar.graph.Mapper;
import io.reflectoring.coderadar.graph.projectadministration.domain.FilePatternEntity;

public class FilePatternMapper implements Mapper<FilePattern, FilePatternEntity> {

  @Override
  public FilePattern mapGraphObject(FilePatternEntity nodeEntity) {
    FilePattern filePattern = new FilePattern();
    filePattern.setId(nodeEntity.getId());
    filePattern.setInclusionType(nodeEntity.getInclusionType());
    filePattern.setPattern(nodeEntity.getPattern());
    return filePattern;
  }

  @Override
  public FilePatternEntity mapDomainObject(FilePattern domainObject) {
    FilePatternEntity filePatternEntity = new FilePatternEntity();
    filePatternEntity.setInclusionType(domainObject.getInclusionType());
    filePatternEntity.setPattern(domainObject.getPattern());
    return filePatternEntity;
  }
}
