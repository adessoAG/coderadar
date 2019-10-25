package io.reflectoring.coderadar.rest.analyzerconfig;

import io.reflectoring.coderadar.plugin.api.AnalyzerConfigurationException;
import io.reflectoring.coderadar.projectadministration.port.driver.analyzerconfig.create.CreateAnalyzerConfigurationCommand;
import io.reflectoring.coderadar.projectadministration.port.driver.analyzerconfig.create.CreateAnalyzerConfigurationUseCase;
import io.reflectoring.coderadar.rest.ErrorMessageResponse;
import io.reflectoring.coderadar.rest.IdResponse;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/projects/{projectId}/analyzers")
@Transactional
public class CreateAnalyzerConfigurationController {
  private final CreateAnalyzerConfigurationUseCase createAnalyzerConfigurationUseCase;

  @Autowired
  public CreateAnalyzerConfigurationController(
      CreateAnalyzerConfigurationUseCase addAnalyzerConfigurationUseCase) {
    this.createAnalyzerConfigurationUseCase = addAnalyzerConfigurationUseCase;
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity addAnalyzerConfiguration(
      @RequestBody @Validated CreateAnalyzerConfigurationCommand command,
      @PathVariable Long projectId) {
    try {
      return new ResponseEntity<>(
              new IdResponse(createAnalyzerConfigurationUseCase.create(command, projectId)),
              HttpStatus.CREATED);
    } catch (AnalyzerConfigurationException e){
      return new ResponseEntity<>(new ErrorMessageResponse(e.getMessage()), HttpStatus.CONFLICT);
    }
  }
}
