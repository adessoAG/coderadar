package io.reflectoring.coderadar.rest.unit.analyzerconfig;

import static org.mockito.Mockito.mock;

import io.reflectoring.coderadar.analyzer.domain.AnalyzerConfiguration;
import io.reflectoring.coderadar.projectadministration.port.driver.analyzerconfig.get.ListAnalyzerConfigurationsUseCase;
import io.reflectoring.coderadar.rest.analyzerconfig.ListAnalyzerConfigurationsFromProjectController;
import io.reflectoring.coderadar.rest.domain.GetAnalyzerConfigurationResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ListAnalyzerConfigurationsFromProjectControllerTest {

  private final ListAnalyzerConfigurationsUseCase listAnalyzerConfigurationsUseCase =
      mock(ListAnalyzerConfigurationsUseCase.class);

  @Test
  void testListAnalyzerConfigurationsFromProject() {
    ListAnalyzerConfigurationsFromProjectController testSubject =
        new ListAnalyzerConfigurationsFromProjectController(listAnalyzerConfigurationsUseCase);

    AnalyzerConfiguration response1 = new AnalyzerConfiguration(1L, "analyzer1", true);
    AnalyzerConfiguration response2 = new AnalyzerConfiguration(2L, "analyzer2", false);
    List<AnalyzerConfiguration> responses = new ArrayList<>();

    responses.add(response1);
    responses.add(response2);

    Mockito.when(listAnalyzerConfigurationsUseCase.get(1L)).thenReturn(responses);

    ResponseEntity<List<GetAnalyzerConfigurationResponse>> responseEntity =
        testSubject.getAnalyzerConfigurationsFromProject(1L);

    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    Assertions.assertNotNull(responseEntity.getBody());
    Assertions.assertEquals(responses.size(), responseEntity.getBody().size());
    Assertions.assertEquals(response1.getId(), responseEntity.getBody().get(0).getId());
    Assertions.assertEquals(
        response1.getAnalyzerName(), responseEntity.getBody().get(0).getAnalyzerName());
    Assertions.assertEquals(response1.isEnabled(), responseEntity.getBody().get(0).isEnabled());
    Assertions.assertEquals(response2.getId(), responseEntity.getBody().get(1).getId());
    Assertions.assertEquals(
        response2.getAnalyzerName(), responseEntity.getBody().get(1).getAnalyzerName());
    Assertions.assertEquals(response2.isEnabled(), responseEntity.getBody().get(1).isEnabled());
  }
}
