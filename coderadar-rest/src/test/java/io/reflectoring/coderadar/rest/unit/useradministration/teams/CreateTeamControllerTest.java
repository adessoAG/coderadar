package io.reflectoring.coderadar.rest.unit.useradministration.teams;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import io.reflectoring.coderadar.rest.domain.IdResponse;
import io.reflectoring.coderadar.rest.unit.UnitTestTemplate;
import io.reflectoring.coderadar.rest.useradministration.teams.CreateTeamController;
import io.reflectoring.coderadar.useradministration.UserNotFoundException;
import io.reflectoring.coderadar.useradministration.port.driver.teams.create.CreateTeamCommand;
import io.reflectoring.coderadar.useradministration.port.driver.teams.create.CreateTeamUseCase;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class CreateTeamControllerTest extends UnitTestTemplate {
  private final CreateTeamUseCase createTeamUseCase = mock(CreateTeamUseCase.class);

  private final CreateTeamController testController = new CreateTeamController(createTeamUseCase);

  @Test
  void testCreateTeam() {

    // Set up
    CreateTeamCommand testCommand = new CreateTeamCommand();
    testCommand.setName("TestTeam1");
    testCommand.setUserIds(Collections.singletonList(1L));

    Mockito.when(createTeamUseCase.createTeam(testCommand)).thenReturn(2L);

    // Test
    ResponseEntity<IdResponse> responseEntity = testController.createTeam(testCommand);
    Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    Assertions.assertNotNull(responseEntity.getBody());
    Assertions.assertEquals(2L, responseEntity.getBody().getId());
  }

  @Test
  void testCreateTeamThrowsExceptionWhenUserNotFound() {
    Mockito.when(createTeamUseCase.createTeam(any())).thenThrow(new UserNotFoundException(1L));
    CreateTeamCommand command = new CreateTeamCommand();
    Assertions.assertThrows(UserNotFoundException.class, () -> testController.createTeam(command));
  }
}
