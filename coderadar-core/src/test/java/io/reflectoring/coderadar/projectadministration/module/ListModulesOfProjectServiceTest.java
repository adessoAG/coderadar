package io.reflectoring.coderadar.projectadministration.module;

import io.reflectoring.coderadar.projectadministration.domain.Module;
import io.reflectoring.coderadar.projectadministration.port.driven.module.ListModulesOfProjectPort;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.projectadministration.service.module.ListModulesOfProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListModulesOfProjectServiceTest {

  @Mock private ListModulesOfProjectPort listModulesPortMock;
  @Mock private GetProjectPort getProjectPort;

  private ListModulesOfProjectService testSubject;

  @BeforeEach
  void setUp() {
    this.testSubject = new ListModulesOfProjectService(listModulesPortMock, getProjectPort);
  }

  @Test
  void returnsTwoModulesFromProject() {
    // given
    long projectId = 1234L;

    Module expectedResponse1 = new Module(1L, "module-path-one");
    Module expectedResponse2 = new Module(2L, "module-path-two");

    when(getProjectPort.existsById(anyLong())).thenReturn(true);

    when(listModulesPortMock.listModules(projectId))
        .thenReturn(Arrays.asList(expectedResponse1, expectedResponse2));

    // when
    List<Module> actualResponses = testSubject.listModules(projectId);

    // then
    assertThat(actualResponses).containsExactly(expectedResponse1, expectedResponse2);
  }
}
