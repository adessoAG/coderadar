package io.reflectoring.coderadar.rest.filepattern;

import io.reflectoring.coderadar.projectadministration.port.driver.filepattern.create.CreateFilePatternCommand;
import io.reflectoring.coderadar.projectadministration.port.driver.filepattern.create.CreateFilePatternUseCase;
import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.rest.domain.IdResponse;
import io.reflectoring.coderadar.useradministration.service.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateFilePatternController implements AbstractBaseController {
  private final CreateFilePatternUseCase createFilePatternUseCase;
  private final AuthenticationService authenticationService;

  @PostMapping(
      path = "/projects/{projectId}/filePatterns",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<IdResponse> createFilePattern(
      @RequestBody @Validated CreateFilePatternCommand command,
      @PathVariable(name = "projectId") long projectId) {
    authenticationService.authenticateAdmin(projectId);
    return new ResponseEntity<>(
        new IdResponse(createFilePatternUseCase.createFilePattern(command, projectId)),
        HttpStatus.CREATED);
  }
}
