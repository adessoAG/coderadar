package io.reflectoring.coderadar.projectadministration.port.driven.filepattern;

import io.reflectoring.coderadar.projectadministration.domain.FilePattern;
import io.reflectoring.coderadar.projectadministration.port.driver.filepattern.get.GetFilePatternResponse;
import java.util.List;

public interface ListFilePatternsOfProjectPort {
  List<FilePattern> listFilePatterns(Long projectId);

  List<GetFilePatternResponse> listFilePatternResponses(Long projectId);
}
