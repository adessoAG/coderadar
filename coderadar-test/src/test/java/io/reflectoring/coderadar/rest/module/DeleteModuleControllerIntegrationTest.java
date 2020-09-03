package io.reflectoring.coderadar.rest.module;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.reflectoring.coderadar.graph.projectadministration.domain.ModuleEntity;
import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.module.repository.ModuleRepository;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.ProjectRepository;
import io.reflectoring.coderadar.rest.ControllerTestTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class DeleteModuleControllerIntegrationTest extends ControllerTestTemplate {

  @Autowired private ProjectRepository projectRepository;

  @Autowired private ModuleRepository moduleRepository;

  @Test
  void deleteModuleWithId() throws Exception {
    // Set up
    ProjectEntity testProject = new ProjectEntity();
    testProject.setVcsUrl("https://valid.url");
    projectRepository.save(testProject);

    ModuleEntity module = new ModuleEntity();
    module.setPath("test-module");
    module.setProject(testProject);
    module = moduleRepository.save(module);
    final Long id = module.getId();

    // Test
    mvc()
        .perform(delete("/api/projects/" + testProject.getId() + "/modules/" + module.getId()))
        .andExpect(status().isOk())
        .andDo(result -> Assertions.assertFalse(moduleRepository.findById(id).isPresent()))
        .andDo(document("modules/delete"));
  }

  @Test
  void deleteModuleReturnsErrorWhenModuleNotFound() throws Exception {
    ProjectEntity testProject = new ProjectEntity();
    testProject.setId(0L);
    projectRepository.save(testProject);

    mvc()
        .perform(delete("/api/projects/0/modules/0"))
        .andExpect(status().isNotFound())
        .andExpect(
            MockMvcResultMatchers.jsonPath("errorMessage").value("Module with id 0 not found."));
  }
}
