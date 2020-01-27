package io.reflectoring.coderadar.rest.query;

import io.reflectoring.coderadar.query.domain.MetricTree;
import io.reflectoring.coderadar.query.port.driver.GetMetricTreeForCommitUseCase;
import io.reflectoring.coderadar.query.port.driver.GetMetricsForCommitCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
public class GetMetricTreeForCommitController {
  private final GetMetricTreeForCommitUseCase getMetricTreeForCommitUseCase;

  public GetMetricTreeForCommitController(
      GetMetricTreeForCommitUseCase getMetricTreeForCommitUseCase) {
    this.getMetricTreeForCommitUseCase = getMetricTreeForCommitUseCase;
  }

  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = "/projects/{projectId}/metricvalues/tree", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MetricTree> getMetricValues(@Validated @RequestBody GetMetricsForCommitCommand command, @PathVariable("projectId") Long projectId){
    return new ResponseEntity<>(getMetricTreeForCommitUseCase.get(command, projectId), HttpStatus.OK);
  }
}
