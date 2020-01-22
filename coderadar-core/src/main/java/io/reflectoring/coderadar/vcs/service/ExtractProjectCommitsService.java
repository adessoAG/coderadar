package io.reflectoring.coderadar.vcs.service;

import io.reflectoring.coderadar.projectadministration.domain.Commit;
import io.reflectoring.coderadar.query.domain.DateRange;
import io.reflectoring.coderadar.vcs.port.driven.ExtractProjectCommitsPort;
import io.reflectoring.coderadar.vcs.port.driver.ExtractProjectCommitsUseCase;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ExtractProjectCommitsService implements ExtractProjectCommitsUseCase {

  private final ExtractProjectCommitsPort extractProjectCommitsPort;

  public ExtractProjectCommitsService(ExtractProjectCommitsPort extractProjectCommitsPort) {
    this.extractProjectCommitsPort = extractProjectCommitsPort;
  }

  @Override
  public List<Commit> getCommits(File repositoryRoot, DateRange range) {
    return extractProjectCommitsPort.extractCommits(repositoryRoot, range);
  }
}
