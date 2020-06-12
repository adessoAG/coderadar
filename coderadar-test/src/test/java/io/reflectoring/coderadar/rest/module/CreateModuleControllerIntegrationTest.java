package io.reflectoring.coderadar.rest.module;

import io.reflectoring.coderadar.graph.projectadministration.domain.FileEntity;
import io.reflectoring.coderadar.graph.projectadministration.domain.ModuleEntity;
import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.module.repository.ModuleRepository;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.ProjectRepository;
import io.reflectoring.coderadar.projectadministration.port.driver.module.create.CreateModuleCommand;
import io.reflectoring.coderadar.rest.ControllerTestTemplate;
import io.reflectoring.coderadar.rest.domain.IdResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static io.reflectoring.coderadar.rest.JsonHelper.fromJson;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class CreateModuleControllerIntegrationTest extends ControllerTestTemplate {

  @Autowired private ProjectRepository projectRepository;
  @Autowired private ModuleRepository moduleRepository;

  @Test
  void createModuleSuccessfully() throws Exception {
    // Set up
    ProjectEntity testProject = new ProjectEntity();
    FileEntity fileEntity = new FileEntity();
    fileEntity.setPath("module-path/Main.java");
    testProject.setVcsUrl("https://valid.url");
    testProject.getFiles().add(fileEntity);
    testProject = projectRepository.save(testProject);

    ConstrainedFields fields = fields(CreateModuleCommand.class);

    // Test
    CreateModuleCommand command = new CreateModuleCommand("module-path");
    mvc()
        .perform(
            post("/api/projects/" + testProject.getId() + "/modules")
                .content(toJson(command))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andDo(
            result -> {
              Long id =
                  fromJson(result.getResponse().getContentAsString(), IdResponse.class).getId();
              ModuleEntity module = moduleRepository.findById(id).get();
              Assertions.assertEquals("module-path/", module.getPath());
            })
        .andDo(
            document(
                "modules/create",
                requestFields(
                    fields
                        .withPath("path")
                        .description(
                            "The path of this module starting at the VCS root. All files below that path are considered to be part of the module."))));
  }

  @Test
  void createModuleSuccessfullyWithNonExistentPath() throws Exception {
    // Set up
    ProjectEntity testProject = new ProjectEntity();
    FileEntity fileEntity = new FileEntity();
    fileEntity.setPath("module-path/Main.java");
    testProject.setVcsUrl("https://valid.url");
    testProject.getFiles().add(fileEntity);
    testProject = projectRepository.save(testProject);

    // Test
    CreateModuleCommand command = new CreateModuleCommand("module-path1");
    mvc()
        .perform(
            post("/api/projects/" + testProject.getId() + "/modules")
                .content(toJson(command))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andDo(
            result -> {
              Long id =
                  fromJson(result.getResponse().getContentAsString(), IdResponse.class).getId();
              ModuleEntity module = moduleRepository.findById(id).get();
              Assertions.assertEquals("module-path1/", module.getPath());
            })
        .andReturn();

    Assertions.assertEquals(
        1L, moduleRepository.findModulesInProjectSortedDesc(testProject.getId()).size());

    List<ModuleEntity> allModules = new ArrayList<>();
    for (ModuleEntity m : moduleRepository.findAll()) {
      allModules.add(m);
    }
    Assertions.assertEquals(1L, allModules.size());
  }

  @Test
  void createModuleReturnsErrorWhenProjectNotFound() throws Exception {
    CreateModuleCommand command = new CreateModuleCommand("module-path");
    mvc()
        .perform(
            post("/api/projects/1/modules")
                .content(toJson(command))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(
            MockMvcResultMatchers.jsonPath("errorMessage").value("Project with id 1 not found."));
  }

  @Test
  void createModuleReturnsErrorWhenRequestIsInvalid() throws Exception {
    CreateModuleCommand command = new CreateModuleCommand("");
    mvc()
        .perform(
            post("/api/projects/0/modules")
                .content(toJson(command))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
