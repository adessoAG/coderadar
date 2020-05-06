package io.reflectoring.coderadar.rest.contributor;

import static io.reflectoring.coderadar.rest.GetContributorResponseMapper.mapContributor;

import io.reflectoring.coderadar.contributor.port.driver.GetContributorUseCase;
import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.rest.domain.GetContributorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class GetContributorController implements AbstractBaseController {
  private final GetContributorUseCase getContributorUseCase;

  public GetContributorController(GetContributorUseCase getContributorUseCase) {
    this.getContributorUseCase = getContributorUseCase;
  }

  @GetMapping(path = "/contributors/{id}")
  public ResponseEntity<GetContributorResponse> getById(@PathVariable long id) {
    return new ResponseEntity<>(mapContributor(getContributorUseCase.getById(id)), HttpStatus.OK);
  }
}
