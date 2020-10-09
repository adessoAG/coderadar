package io.reflectoring.coderadar.rest.useradministration.permissions;

import static io.reflectoring.coderadar.rest.JsonHelper.fromJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.ProjectRepository;
import io.reflectoring.coderadar.graph.useradministration.domain.UserEntity;
import io.reflectoring.coderadar.graph.useradministration.repository.UserRepository;
import io.reflectoring.coderadar.rest.ControllerTestTemplate;
import io.reflectoring.coderadar.rest.domain.ErrorMessageResponse;
import io.reflectoring.coderadar.useradministration.service.security.PasswordUtil;
import java.util.Collections;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

class RemoveUserFromProjectControllerIntegrationTest extends ControllerTestTemplate {

  @Autowired private UserRepository userRepository;

  @Autowired private ProjectRepository projectRepository;

  private ProjectEntity testProject;
  private UserEntity testUser;

  @BeforeEach
  void setUp() {
    testProject = new ProjectEntity();
    testProject.setVcsUrl("https://valid.url");
    testProject.setName("project");
    testProject.setVcsStart(new Date());
    testProject.setVcsUsername("testUser");
    projectRepository.save(testProject);

    testUser = new UserEntity();
    testUser.setUsername("username");
    testUser.setPassword(PasswordUtil.hash("password1"));
    testUser.setProjects(Collections.singletonList(testProject));
    userRepository.save(testUser);
  }

  @Test
  void removeUserFromProjectSuccessfully() throws Exception {
    mvc()
        .perform(delete("/api/projects/" + testProject.getId() + "/users/" + testUser.getId()))
        .andExpect(status().isOk())
        .andDo(document("user/role/project/remove"))
        .andReturn();

    Assertions.assertTrue(userRepository.listUsersForProject(testProject.getId()).isEmpty());
  }

  @Test
  void throwsExceptionWhenUserIsNotAssignedToProject() throws Exception {
    testUser.setProjects(Collections.emptyList());
    userRepository.save(testUser, 1);

    MvcResult result =
        mvc()
            .perform(delete("/api/projects/" + testProject.getId() + "/users/" + testUser.getId()))
            .andExpect(status().isBadRequest())
            .andReturn();

    String errorMessage =
        fromJson(result.getResponse().getContentAsString(), ErrorMessageResponse.class)
            .getErrorMessage();

    Assertions.assertEquals(
        "User with id "
            + testUser.getId()
            + " is not assigned to project with id "
            + testProject.getId(),
        errorMessage);
  }

  @Test
  void throwsExceptionWhenProjectDoesNotExist() throws Exception {
    MvcResult result =
        mvc()
            .perform(delete("/api/projects/1000/users/" + testUser.getId()))
            .andExpect(status().isNotFound())
            .andReturn();

    String errorMessage =
        fromJson(result.getResponse().getContentAsString(), ErrorMessageResponse.class)
            .getErrorMessage();
    Assertions.assertEquals("Project with id 1000 not found.", errorMessage);
  }

  @Test
  void throwsExceptionWhenUserDoesNotExist() throws Exception {
    MvcResult result =
        mvc()
            .perform(delete("/api/projects/" + testProject.getId() + "/users/1000"))
            .andExpect(status().isNotFound())
            .andReturn();

    String errorMessage =
        fromJson(result.getResponse().getContentAsString(), ErrorMessageResponse.class)
            .getErrorMessage();
    Assertions.assertEquals("User with id 1000 not found.", errorMessage);
  }
}
