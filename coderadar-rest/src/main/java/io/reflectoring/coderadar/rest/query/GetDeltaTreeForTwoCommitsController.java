package io.reflectoring.coderadar.rest.query;

import io.reflectoring.coderadar.query.domain.DeltaTree;
import io.reflectoring.coderadar.query.port.driver.deltatree.GetDeltaTreeForTwoCommitsCommand;
import io.reflectoring.coderadar.query.port.driver.deltatree.GetDeltaTreeForTwoCommitsUseCase;
import io.reflectoring.coderadar.rest.AbstractBaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Transactional
@RestController
public class GetDeltaTreeForTwoCommitsController implements AbstractBaseController {

  private final GetDeltaTreeForTwoCommitsUseCase getDeltaTreeForTwoCommitsUseCase;

  public GetDeltaTreeForTwoCommitsController(
      GetDeltaTreeForTwoCommitsUseCase getDeltaTreeForTwoCommitsUseCase) {
    this.getDeltaTreeForTwoCommitsUseCase = getDeltaTreeForTwoCommitsUseCase;
  }

  @RequestMapping(
      method = {RequestMethod.POST, RequestMethod.GET},
      path = "/projects/{projectId}/metricvalues/deltaTree",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DeltaTree> getMetricValuesForTwoCommits(
      @Validated @RequestBody GetDeltaTreeForTwoCommitsCommand command,
      @PathVariable("projectId") long projectId) {
    return new ResponseEntity<>(
        getDeltaTreeForTwoCommitsUseCase.get(command, projectId), HttpStatus.OK);
  }
}
