package io.reflectoring.coderadar.rest.useradministration.teams;

import static io.reflectoring.coderadar.rest.JsonHelper.fromJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.reflectoring.coderadar.graph.useradministration.domain.TeamEntity;
import io.reflectoring.coderadar.graph.useradministration.domain.UserEntity;
import io.reflectoring.coderadar.graph.useradministration.repository.TeamRepository;
import io.reflectoring.coderadar.graph.useradministration.repository.UserRepository;
import io.reflectoring.coderadar.rest.ControllerTestTemplate;
import io.reflectoring.coderadar.rest.domain.ErrorMessageResponse;
import io.reflectoring.coderadar.useradministration.service.security.PasswordUtil;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

class DeleteTeamControllerIntegrationTest extends ControllerTestTemplate {

  @Autowired private UserRepository userRepository;

  @Autowired private TeamRepository teamRepository;

  private UserEntity testUser;

  private TeamEntity teamEntity;

  @BeforeEach
  void setUp() {
    teamEntity = new TeamEntity();
    teamEntity.setName("testTeam");
    teamRepository.save(teamEntity, 1);

    testUser = new UserEntity();
    testUser.setUsername("username");
    testUser.setPassword(PasswordUtil.hash("password1"));
    userRepository.save(testUser);
  }

  @Test
  void deleteTeamWithUsersSuccessfully() throws Exception {
    teamEntity.setMembers(Collections.singletonList(testUser));
    teamRepository.save(teamEntity, 1);

    mvc()
        .perform(delete("/api/teams/" + teamEntity.getId()))
        .andExpect(status().isOk())
        .andDo(document("teams/delete"))
        .andReturn();

    List<TeamEntity> teams = teamRepository.findAllWithMembers();
    Assertions.assertTrue(teams.isEmpty());
  }

  @Test
  void deleteTeamWithNoUsersSuccessfully() throws Exception {
    mvc().perform(delete("/api/teams/" + teamEntity.getId())).andExpect(status().isOk());

    List<TeamEntity> teams = teamRepository.findAllWithMembers();
    Assertions.assertTrue(teams.isEmpty());
  }

  @Test
  void throwsExceptionWhenTeamDoesNotExist() throws Exception {
    MvcResult result =
        mvc().perform(delete("/api/teams/1000")).andExpect(status().isNotFound()).andReturn();

    String errorMessage =
        fromJson(result.getResponse().getContentAsString(), ErrorMessageResponse.class)
            .getErrorMessage();
    Assertions.assertEquals("Team with id 1000 not found.", errorMessage);
  }
}
