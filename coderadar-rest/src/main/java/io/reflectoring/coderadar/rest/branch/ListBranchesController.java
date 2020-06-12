package io.reflectoring.coderadar.rest.branch;

import io.reflectoring.coderadar.projectadministration.domain.Branch;
import io.reflectoring.coderadar.projectadministration.port.driver.branch.list.ListBranchesUseCase;
import io.reflectoring.coderadar.rest.AbstractBaseController;
import io.reflectoring.coderadar.rest.domain.GetBranchResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.reflectoring.coderadar.rest.GetBranchResponseMapper.mapBranches;

@Transactional
@RestController
public class ListBranchesController implements AbstractBaseController {
  private final ListBranchesUseCase listBranchesUseCase;

  public ListBranchesController(ListBranchesUseCase listBranchesUseCase) {
    this.listBranchesUseCase = listBranchesUseCase;
  }

  @GetMapping(path = "/projects/{projectId}/branches", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<GetBranchResponse>> listBranches(@PathVariable long projectId) {
    List<Branch> branches = listBranchesUseCase.listBranchesInProject(projectId);
    return new ResponseEntity<>(mapBranches(branches), HttpStatus.OK);
  }
}
