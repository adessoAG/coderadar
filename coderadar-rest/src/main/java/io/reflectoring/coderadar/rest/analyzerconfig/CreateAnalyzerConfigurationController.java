package io.reflectoring.coderadar.rest.analyzerconfig;

import io.reflectoring.coderadar.plugin.api.AnalyzerConfigurationException;
import io.reflectoring.coderadar.projectadministration.port.driver.analyzerconfig.create.CreateAnalyzerConfigurationCommand;
import io.reflectoring.coderadar.projectadministration.port.driver.analyzerconfig.create.CreateAnalyzerConfigurationUseCase;
import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.rest.domain.ErrorMessageResponse;
import io.reflectoring.coderadar.rest.domain.IdResponse;
import io.reflectoring.coderadar.useradministration.service.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CreateAnalyzerConfigurationController implements AbstractBaseController {
  private final CreateAnalyzerConfigurationUseCase createAnalyzerConfigurationUseCase;
  private final AuthenticationService authenticationService;

  @PostMapping(
      path = "/projects/{projectId}/analyzers",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> addAnalyzerConfiguration(
      @RequestBody @Validated CreateAnalyzerConfigurationCommand command,
      @PathVariable long projectId) {
    authenticationService.authenticateAdmin(projectId);
    try {
      return new ResponseEntity<>(
          new IdResponse(createAnalyzerConfigurationUseCase.create(command, projectId)),
          HttpStatus.CREATED);
    } catch (AnalyzerConfigurationException e) {
      return new ResponseEntity<>(new ErrorMessageResponse(e.getMessage()), HttpStatus.CONFLICT);
    }
  }
}
