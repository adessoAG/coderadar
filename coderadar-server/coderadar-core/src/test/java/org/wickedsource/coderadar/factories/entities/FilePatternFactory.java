package org.wickedsource.coderadar.factories.entities;

import org.wickedsource.coderadar.filepattern.domain.FilePattern;
import org.wickedsource.coderadar.project.domain.InclusionType;

public class FilePatternFactory {

  public FilePattern filePattern() {
    FilePattern pattern = new FilePattern();
    pattern.setPattern("src/main/java/**/*.java");
    pattern.setInclusionType(InclusionType.INCLUDE);
    return pattern;
  }

  public FilePattern filePattern2() {
    FilePattern pattern = new FilePattern();
    pattern.setPattern("src/main/java/**/generated/**/*.java");
    pattern.setInclusionType(InclusionType.EXCLUDE);
    return pattern;
  }
}
