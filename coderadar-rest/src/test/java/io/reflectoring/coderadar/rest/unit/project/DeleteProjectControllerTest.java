package io.reflectoring.coderadar.rest.unit.project;

import static org.mockito.Mockito.mock;

import io.reflectoring.coderadar.projectadministration.port.driver.project.delete.DeleteProjectUseCase;
import io.reflectoring.coderadar.rest.project.DeleteProjectController;
import io.reflectoring.coderadar.rest.unit.UnitTestTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class DeleteProjectControllerTest extends UnitTestTemplate {

  private final DeleteProjectUseCase deleteProjectUseCase = mock(DeleteProjectUseCase.class);

  @Test
  void deleteProjectWithIdOne() {
    DeleteProjectController testSubject =
        new DeleteProjectController(deleteProjectUseCase, authenticationService);

    ResponseEntity<HttpStatus> responseEntity = testSubject.deleteProject(1L);
    Mockito.verify(deleteProjectUseCase, Mockito.times(1)).delete(1L);

    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}
