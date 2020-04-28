package io.reflectoring.coderadar.rest.filepattern;

import io.reflectoring.coderadar.projectadministration.port.driver.filepattern.delete.DeleteFilePatternFromProjectUseCase;
import io.reflectoring.coderadar.rest.AbstractBaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class DeleteFilePatternController implements AbstractBaseController {
  private final DeleteFilePatternFromProjectUseCase deleteFilePatternFromProjectUseCase;

  public DeleteFilePatternController(
      DeleteFilePatternFromProjectUseCase deleteFilePatternFromProjectUseCase) {
    this.deleteFilePatternFromProjectUseCase = deleteFilePatternFromProjectUseCase;
  }

  @DeleteMapping(path = "/projects/{projectId}/filePatterns/{filePatternId}")
  public ResponseEntity<HttpStatus> deleteFilePattern(
      @PathVariable(name = "filePatternId") long filePatternId,
      @PathVariable(name = "projectId") long projectId) {
    deleteFilePatternFromProjectUseCase.delete(filePatternId, projectId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
