package io.reflectoring.coderadar.projectadministration.project;

import io.reflectoring.coderadar.projectadministration.domain.Project;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.projectadministration.service.project.GetProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProjectServiceTest {

  @Mock private GetProjectPort getProjectPortMock;

  private GetProjectService testSubject;

  @BeforeEach
  void setUp() {
    this.testSubject = new GetProjectService(getProjectPortMock);
  }

  @Test
  void returnsGetProjectResponseWithIdOne() {
    // given
    long projectId = 1L;
    String projectName = "project name";
    String vcsUrl = "http://valid.url";
    String vcsUsername = "username";
    String vcsPassword = "password";
    Date startDate = new Date();
    Date endDate = new Date();

    Project expectedResponse =
        new Project()
            .setId(projectId)
            .setName(projectName)
            .setVcsUrl(vcsUrl)
            .setVcsUsername(vcsUsername)
            .setVcsPassword(vcsPassword)
            .setVcsOnline(true)
            .setVcsStart(startDate)
            .setVcsEnd(endDate);

    when(getProjectPortMock.get(projectId)).thenReturn(expectedResponse);

    // when
    Project actualResponse = testSubject.get(1L);

    // then
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }
}
