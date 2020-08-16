package io.reflectoring.coderadar.rest.project;

import static io.reflectoring.coderadar.rest.JsonHelper.fromJson;
import static io.reflectoring.coderadar.rest.ResultMatchers.containsResource;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.ProjectRepository;
import io.reflectoring.coderadar.rest.ControllerTestTemplate;
import io.reflectoring.coderadar.rest.domain.GetProjectResponse;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ListProjectsControllerIntegrationTest extends ControllerTestTemplate {

  @Autowired private ProjectRepository projectRepository;

  @BeforeEach
  public void setUp() {
    ProjectEntity testProject = new ProjectEntity();
    testProject.setVcsUrl("https://valid.url");
    testProject.setName("project");
    testProject.setVcsStart(new Date());
    testProject.setVcsPassword("testPassword");
    testProject.setVcsUsername("testUser");

    ProjectEntity testProject2 = new ProjectEntity();
    testProject2.setVcsUrl("https://valid.url");
    testProject2.setName("project");
    testProject2.setVcsStart(new Date());
    testProject2.setVcsPassword("testPassword");
    testProject2.setVcsUsername("testUser");

    projectRepository.save(testProject);
    projectRepository.save(testProject2);
  }

  @Test
  void listAllProjects() throws Exception {
    mvc()
        .perform(get("/api/projects"))
        .andExpect(status().isOk())
        .andExpect(containsResource(GetProjectResponse[].class))
        .andExpect(
            result -> {
              GetProjectResponse[] responses =
                  fromJson(result.getResponse().getContentAsString(), GetProjectResponse[].class);
              Assertions.assertEquals(2, responses.length);
            })
        .andDo(document("projects/list"));
  }
}
