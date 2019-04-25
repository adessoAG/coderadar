package io.reflectoring.coderadar.rest.project;

import io.reflectoring.coderadar.core.projectadministration.domain.Project;
import io.reflectoring.coderadar.core.projectadministration.port.driver.project.ListProjectsUseCase;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/projects")
public class ListProjectsController {
  private final ListProjectsUseCase listProjectsUseCase;

  @Autowired
  public ListProjectsController(ListProjectsUseCase listProjectsUseCase) {
    this.listProjectsUseCase = listProjectsUseCase;
  }

  @GetMapping
  public ResponseEntity<List<Project>> listProjects() {
    return new ResponseEntity<>(listProjectsUseCase.listProjects(), HttpStatus.OK);
  }
}
