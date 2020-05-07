package io.reflectoring.coderadar.graph;

import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.project.ProjectMapper;
import io.reflectoring.coderadar.projectadministration.domain.Project;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProjectMapperTest {
  private final ProjectMapper projectMapper = new ProjectMapper();

  @Test
  public void testMapDomainObject() {
    Project testProject =
        new Project()
            .setId(1L)
            .setName("testName")
            .setVcsStart(new Date(123L))
            .setVcsPassword("testPassword")
            .setVcsUsername("testUsername")
            .setVcsOnline(true)
            .setWorkdirName("workdir")
            .setVcsUrl("testUrl");

    ProjectEntity result = projectMapper.mapDomainObject(testProject);
    Assertions.assertEquals("testName", result.getName());
    Assertions.assertEquals("testUsername", result.getVcsUsername());
    Assertions.assertEquals("testPassword", result.getVcsPassword());
    Assertions.assertEquals("testUrl", result.getVcsUrl());
    Assertions.assertEquals(new Date(123L), result.getVcsStart());
    Assertions.assertEquals("workdir", result.getWorkdirName());
    Assertions.assertTrue(result.isVcsOnline());
    Assertions.assertNull(result.getId());
  }

  @Test
  public void testMapGraphObject() {
    ProjectEntity testProject =
        new ProjectEntity()
            .setId(1L)
            .setName("testName")
            .setVcsStart(new Date(123L))
            .setVcsPassword("testPassword")
            .setVcsUsername("testUsername")
            .setVcsOnline(true)
            .setWorkdirName("workdir")
            .setVcsUrl("testUrl");
    Project result = projectMapper.mapGraphObject(testProject);
    Assertions.assertEquals("testName", result.getName());
    Assertions.assertEquals("testUsername", result.getVcsUsername());
    Assertions.assertEquals("testPassword", result.getVcsPassword());
    Assertions.assertEquals("testUrl", result.getVcsUrl());
    Assertions.assertEquals(new Date(123L), result.getVcsStart());
    Assertions.assertEquals("workdir", result.getWorkdirName());
    Assertions.assertTrue(result.isVcsOnline());
    Assertions.assertEquals(1L, result.getId());
  }
}
