package io.reflectoring.coderadar.rest.analyzing;

import io.reflectoring.coderadar.analyzer.port.driver.ListAnalyzersUseCase;
import io.reflectoring.coderadar.rest.AbstractBaseController;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ListAnalyzersController implements AbstractBaseController {
  private final ListAnalyzersUseCase listAnalyzersUseCase;

  @GetMapping(path = "/analyzers", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<String>> listAvailableAnalyzers() {
    return new ResponseEntity<>(listAnalyzersUseCase.listAvailableAnalyzers(), HttpStatus.OK);
  }
}
