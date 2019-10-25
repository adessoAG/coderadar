package io.reflectoring.coderadar.rest.analyzerconfig;

import io.reflectoring.coderadar.projectadministration.port.driver.analyzerconfig.get.GetAnalyzerConfigurationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
public class GetAnalyzerConfigurationController {
  private final GetAnalyzerConfigurationUseCase getAnalyzerConfigurationUseCase;

  @Autowired
  public GetAnalyzerConfigurationController(
      GetAnalyzerConfigurationUseCase getAnalyzerConfigurationUseCase) {
    this.getAnalyzerConfigurationUseCase = getAnalyzerConfigurationUseCase;
  }

  @GetMapping(path = "/projects/{projectId}/analyzers/{analyzerConfigurationId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getAnalyzerConfiguration(@PathVariable Long analyzerConfigurationId) {
      return new ResponseEntity<>(
          getAnalyzerConfigurationUseCase.getSingleAnalyzerConfiguration(analyzerConfigurationId),
          HttpStatus.OK);
  }
}
