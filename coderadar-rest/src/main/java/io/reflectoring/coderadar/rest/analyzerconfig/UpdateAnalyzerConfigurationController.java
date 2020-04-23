package io.reflectoring.coderadar.rest.analyzerconfig;

import io.reflectoring.coderadar.plugin.api.AnalyzerConfigurationException;
import io.reflectoring.coderadar.projectadministration.port.driver.analyzerconfig.update.UpdateAnalyzerConfigurationCommand;
import io.reflectoring.coderadar.projectadministration.port.driver.analyzerconfig.update.UpdateAnalyzerConfigurationUseCase;
import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.rest.domain.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class UpdateAnalyzerConfigurationController extends AbstractBaseController {
  private final UpdateAnalyzerConfigurationUseCase updateAnalyzerConfigurationUseCase;

  public UpdateAnalyzerConfigurationController(
      UpdateAnalyzerConfigurationUseCase updateAnalyzerConfigurationUseCase) {
    this.updateAnalyzerConfigurationUseCase = updateAnalyzerConfigurationUseCase;
  }

  @PostMapping(
      path = "/projects/{projectId}/analyzers/{analyzerConfigurationId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> updateAnalyzerConfiguration(
      @RequestBody @Validated UpdateAnalyzerConfigurationCommand command,
      @PathVariable(name = "analyzerConfigurationId") long analyzerConfigurationId,
      @PathVariable(name = "projectId") long projectId) {
    try {
      updateAnalyzerConfigurationUseCase.update(command, analyzerConfigurationId, projectId);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (AnalyzerConfigurationException e) {
      return new ResponseEntity<>(new ErrorMessageResponse(e.getMessage()), HttpStatus.CONFLICT);
    }
  }
}
