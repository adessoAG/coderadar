package io.reflectoring.coderadar.rest.unit.filepattern;

import static org.mockito.Mockito.mock;

import io.reflectoring.coderadar.projectadministration.domain.FilePattern;
import io.reflectoring.coderadar.projectadministration.domain.InclusionType;
import io.reflectoring.coderadar.projectadministration.port.driver.filepattern.get.ListFilePatternsOfProjectUseCase;
import io.reflectoring.coderadar.rest.domain.GetFilePatternResponse;
import io.reflectoring.coderadar.rest.filepattern.ListFilePatternsOfProjectController;
import io.reflectoring.coderadar.rest.unit.UnitTestTemplate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ListFilePatternsOfProjectControllerTest extends UnitTestTemplate {

  private final ListFilePatternsOfProjectUseCase listFilePatternsOfProjectUseCase =
      mock(ListFilePatternsOfProjectUseCase.class);

  @Test
  void testListFilePatternsOfProject() {
    ListFilePatternsOfProjectController testSubject =
        new ListFilePatternsOfProjectController(
            listFilePatternsOfProjectUseCase, authenticationService);

    FilePattern response1 = new FilePattern(1L, "**/*.java", InclusionType.INCLUDE);
    FilePattern response2 = new FilePattern(2L, "**/*.xml", InclusionType.EXCLUDE);
    List<FilePattern> responses = new ArrayList<>();
    responses.add(response1);
    responses.add(response2);

    Mockito.when(listFilePatternsOfProjectUseCase.listFilePatterns(1L)).thenReturn(responses);

    ResponseEntity<List<GetFilePatternResponse>> responseEntity = testSubject.listFilePatterns(1L);

    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    Assertions.assertNotNull(responseEntity.getBody());
    Assertions.assertEquals(responses.size(), responseEntity.getBody().size());
  }
}
