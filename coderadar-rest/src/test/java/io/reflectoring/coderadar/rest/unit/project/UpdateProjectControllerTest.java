package io.reflectoring.coderadar.rest.unit.project;

import static org.mockito.Mockito.mock;

import io.reflectoring.coderadar.projectadministration.port.driver.project.update.UpdateProjectCommand;
import io.reflectoring.coderadar.projectadministration.port.driver.project.update.UpdateProjectUseCase;
import io.reflectoring.coderadar.rest.project.UpdateProjectController;
import io.reflectoring.coderadar.rest.unit.UnitTestTemplate;
import java.net.MalformedURLException;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UpdateProjectControllerTest extends UnitTestTemplate {

  private final UpdateProjectUseCase updateProjectUseCase = mock(UpdateProjectUseCase.class);

  @Test
  void updateProjectWithIdOne() throws MalformedURLException {
    UpdateProjectController testSubject =
        new UpdateProjectController(updateProjectUseCase, authenticationService);

    UpdateProjectCommand command =
        new UpdateProjectCommand(
            "new name", "username", "password", "http://valid.url", true, new Date());
    ResponseEntity<HttpStatus> responseEntity = testSubject.updateProject(command, 1L);

    Mockito.verify(updateProjectUseCase, Mockito.times(1)).update(command, 1L);

    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}
