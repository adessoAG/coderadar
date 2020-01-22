package io.reflectoring.coderadar.graph.projectadministration.filepattern.adapter;

import io.reflectoring.coderadar.graph.projectadministration.filepattern.FilePatternMapper;
import io.reflectoring.coderadar.graph.projectadministration.filepattern.repository.FilePatternRepository;
import io.reflectoring.coderadar.projectadministration.domain.FilePattern;
import io.reflectoring.coderadar.projectadministration.port.driven.filepattern.ListFilePatternsOfProjectPort;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ListFilePatternsOfProjectAdapter implements ListFilePatternsOfProjectPort {
  private final FilePatternRepository filePatternRepository;
  private final FilePatternMapper filePatternMapper = new FilePatternMapper();

  public ListFilePatternsOfProjectAdapter(FilePatternRepository filePatternRepository) {
    this.filePatternRepository = filePatternRepository;
  }

  @Override
  public List<FilePattern> listFilePatterns(Long projectId) {
    return filePatternMapper.mapNodeEntities(filePatternRepository.findByProjectId(projectId));
  }
}
