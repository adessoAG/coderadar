package io.reflectoring.coderadar.projectadministration.analyzerconfig;

import io.reflectoring.coderadar.analyzer.domain.AnalyzerConfiguration;
import io.reflectoring.coderadar.projectadministration.port.driven.analyzerconfig.GetAnalyzerConfigurationPort;
import io.reflectoring.coderadar.projectadministration.service.analyzerconfig.GetAnalyzerConfigurationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAnalyzerConfigurationServiceTest {

  @Mock private GetAnalyzerConfigurationPort getConfigurationPortMock;

  private GetAnalyzerConfigurationService testSubject;

  @BeforeEach
  void setUp() {
    this.testSubject = new GetAnalyzerConfigurationService(getConfigurationPortMock);
  }

  @Test
  void returnsAnalyzerConfigurationWithExpectedId() {
    // given
    long configurationId = 1L;
    String analyzerName = "analyzer";
    boolean analyzerEnabled = true;

    AnalyzerConfiguration analyzerConfiguration =
        new AnalyzerConfiguration()
            .setId(configurationId)
            .setAnalyzerName(analyzerName)
            .setEnabled(analyzerEnabled);

    AnalyzerConfiguration expectedResponse =
        new AnalyzerConfiguration(configurationId, analyzerName, analyzerEnabled);

    when(getConfigurationPortMock.getAnalyzerConfiguration(1L)).thenReturn(analyzerConfiguration);

    // when
    AnalyzerConfiguration actualResponse = testSubject.getAnalyzerConfiguration(1L);

    // then
    assertThat(actualResponse).isEqualTo(expectedResponse);
  }
}
