package io.reflectoring.coderadar.projectadministration.module;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import io.reflectoring.coderadar.projectadministration.ProjectIsBeingProcessedException;
import io.reflectoring.coderadar.projectadministration.domain.Module;
import io.reflectoring.coderadar.projectadministration.port.driven.module.DeleteModulePort;
import io.reflectoring.coderadar.projectadministration.port.driven.module.GetModulePort;
import io.reflectoring.coderadar.projectadministration.service.ProcessProjectService;
import io.reflectoring.coderadar.projectadministration.service.module.DeleteModuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class DeleteModuleServiceTest {

  @Mock private DeleteModulePort deleteModulePortMock;

  @Mock private ProcessProjectService processProjectServiceMock;

  @Mock private GetModulePort getModulePortMock;

  private DeleteModuleService testSubject;

  @BeforeEach
  void setUp() {
    this.testSubject =
        new DeleteModuleService(deleteModulePortMock, processProjectServiceMock, getModulePortMock);
    when(getModulePortMock.get(anyLong())).thenReturn(new Module());
  }

  @Test
  void deleteModuleDeletesModuleWithGivenId() throws ProjectIsBeingProcessedException {
    // given
    long moduleId = 1L;
    long projectId = 2L;

    doAnswer(
            (Answer<Void>)
                invocation -> {
                  Runnable runnable = invocation.getArgument(0);
                  runnable.run();

                  return null;
                })
        .when(processProjectServiceMock)
        .executeTask(any(), eq(projectId));

    // when
    testSubject.delete(moduleId, projectId);

    // then
    verify(deleteModulePortMock).delete(moduleId, projectId);
  }
}
