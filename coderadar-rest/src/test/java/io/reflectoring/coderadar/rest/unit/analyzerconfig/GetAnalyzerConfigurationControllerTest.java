package io.reflectoring.coderadar.rest.unit.analyzerconfig;

import static org.mockito.Mockito.mock;

import io.reflectoring.coderadar.analyzer.domain.AnalyzerConfiguration;
import io.reflectoring.coderadar.projectadministration.port.driver.analyzerconfig.get.GetAnalyzerConfigurationUseCase;
import io.reflectoring.coderadar.rest.analyzerconfig.GetAnalyzerConfigurationController;
import io.reflectoring.coderadar.rest.domain.GetAnalyzerConfigurationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GetAnalyzerConfigurationControllerTest {

  private final GetAnalyzerConfigurationUseCase getAnalyzerConfigurationUseCase =
      mock(GetAnalyzerConfigurationUseCase.class);

  @Test
  void testGetAnalyzerConfiguration() {
    GetAnalyzerConfigurationController testSubject =
        new GetAnalyzerConfigurationController(getAnalyzerConfigurationUseCase);

    AnalyzerConfiguration analyzerConfiguration = new AnalyzerConfiguration(1L, "analyzer", true);

    Mockito.when(getAnalyzerConfigurationUseCase.getAnalyzerConfiguration(1L))
        .thenReturn(analyzerConfiguration);

    ResponseEntity<GetAnalyzerConfigurationResponse> responseEntity =
        testSubject.getAnalyzerConfiguration(1L);

    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    Assertions.assertNotNull(responseEntity.getBody());
    Assertions.assertEquals(1L, responseEntity.getBody().getId());
    Assertions.assertEquals("analyzer", responseEntity.getBody().getAnalyzerName());
    Assertions.assertTrue(responseEntity.getBody().isEnabled());
  }
}
