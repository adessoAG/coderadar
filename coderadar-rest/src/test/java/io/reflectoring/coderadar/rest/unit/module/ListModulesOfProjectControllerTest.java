package io.reflectoring.coderadar.rest.unit.module;

import static org.mockito.Mockito.mock;

import io.reflectoring.coderadar.projectadministration.domain.Module;
import io.reflectoring.coderadar.projectadministration.port.driver.module.get.ListModulesOfProjectUseCase;
import io.reflectoring.coderadar.rest.domain.GetModuleResponse;
import io.reflectoring.coderadar.rest.module.ListModulesOfProjectController;
import io.reflectoring.coderadar.rest.unit.UnitTestTemplate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ListModulesOfProjectControllerTest extends UnitTestTemplate {

  private final ListModulesOfProjectUseCase listModulesOfProjectUseCase =
      mock(ListModulesOfProjectUseCase.class);

  @Test
  void testListModulesOfProject() {
    ListModulesOfProjectController testSubject =
        new ListModulesOfProjectController(listModulesOfProjectUseCase, authenticationService);

    List<Module> responses = new ArrayList<>();
    Module response1 = new Module(1L, "module-path-one");
    Module response2 = new Module(2L, "module-path-two");
    responses.add(response1);
    responses.add(response2);

    Mockito.when(listModulesOfProjectUseCase.listModules(1L)).thenReturn(responses);

    ResponseEntity<List<GetModuleResponse>> responseEntity = testSubject.listModules(1L);

    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    Assertions.assertNotNull(responseEntity.getBody());
    Assertions.assertEquals(responses.size(), responseEntity.getBody().size());
    Assertions.assertEquals(response1.getId(), responseEntity.getBody().get(0).getId());
    Assertions.assertEquals(response1.getPath(), responseEntity.getBody().get(0).getPath());
    Assertions.assertEquals(response2.getId(), responseEntity.getBody().get(1).getId());
    Assertions.assertEquals(response2.getPath(), responseEntity.getBody().get(1).getPath());
  }
}
