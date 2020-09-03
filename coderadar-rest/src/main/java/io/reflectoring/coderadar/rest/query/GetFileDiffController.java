package io.reflectoring.coderadar.rest.query;

import io.reflectoring.coderadar.query.domain.FileContentWithMetrics;
import io.reflectoring.coderadar.query.port.driver.filediff.GetFileDiffCommand;
import io.reflectoring.coderadar.query.port.driver.filediff.GetFileDiffUseCase;
import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.useradministration.service.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Transactional
@RestController
public class GetFileDiffController implements AbstractBaseController {

  private final GetFileDiffUseCase useCase;
  private final AuthenticationService authenticationService;

  public GetFileDiffController(
      GetFileDiffUseCase useCase, AuthenticationService authenticationService) {
    this.useCase = useCase;
    this.authenticationService = authenticationService;
  }

  @RequestMapping(
      method = {RequestMethod.GET, RequestMethod.POST},
      path = "/projects/{projectId}/files/diff",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FileContentWithMetrics> getFileDiff(
      @PathVariable Long projectId, @RequestBody @Validated GetFileDiffCommand command) {
    authenticationService.authenticateMember(projectId);
    return new ResponseEntity<>(useCase.getFileDiff(projectId, command), HttpStatus.OK);
  }
}
