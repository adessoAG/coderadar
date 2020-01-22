package io.reflectoring.coderadar.projectadministration.filepattern;

import io.reflectoring.coderadar.projectadministration.domain.FilePattern;
import io.reflectoring.coderadar.projectadministration.domain.InclusionType;
import io.reflectoring.coderadar.projectadministration.port.driven.filepattern.ListFilePatternsOfProjectPort;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.projectadministration.service.filepattern.ListFilePatternsOfProjectService;
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
class ListFilePatternsOfProjectServiceTest {

  @Mock private ListFilePatternsOfProjectPort listPatternsPortMock;

  @Mock private GetProjectPort getProjectPort;

  private ListFilePatternsOfProjectService testSubject;

  @BeforeEach
  void setUp() {
    this.testSubject = new ListFilePatternsOfProjectService(listPatternsPortMock, getProjectPort);
  }

  @Test
  void returnsTwoFilePatternsFromProject() {
    // given
    long projectId = 123L;

    FilePattern expectedResponse1 = new FilePattern(1L, "**/*.java", InclusionType.INCLUDE);
    FilePattern expectedResponse2 = new FilePattern(2L, "**/*.xml", InclusionType.EXCLUDE);

    when(getProjectPort.existsById(anyLong())).thenReturn(true);

    when(listPatternsPortMock.listFilePatterns(projectId))
        .thenReturn(Arrays.asList(expectedResponse1, expectedResponse2));

    // when
    List<FilePattern> actualResponse = testSubject.listFilePatterns(projectId);

    // then
    assertThat(actualResponse).containsExactly(expectedResponse1, expectedResponse2);
  }
}
