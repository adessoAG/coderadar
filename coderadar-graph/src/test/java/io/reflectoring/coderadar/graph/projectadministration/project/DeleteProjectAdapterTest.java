package io.reflectoring.coderadar.graph.projectadministration.project;

import io.reflectoring.coderadar.CoderadarConfigurationProperties;
import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.ProjectRepository;
import io.reflectoring.coderadar.graph.projectadministration.project.service.DeleteProjectAdapter;
import io.reflectoring.coderadar.projectadministration.domain.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Delete project")
class DeleteProjectAdapterTest {
  private ProjectRepository projectRepository = mock(ProjectRepository.class);
  private CoderadarConfigurationProperties coderadarConfigurationProperties =
      mock(CoderadarConfigurationProperties.class);

  private DeleteProjectAdapter deleteProjectAdapter;

  @BeforeEach
  void setUp() {
    when(projectRepository.findProjectById(anyLong())).thenReturn(Optional.of(new ProjectEntity()));
    deleteProjectAdapter =
        new DeleteProjectAdapter(projectRepository, coderadarConfigurationProperties);
  }

  @Test
  @DisplayName("Should delete project when passing a valid project id")
  void shouldDeleteProjectWhenPassingAValidProjectId() {
    doNothing().when(projectRepository).deleteProjectFindings(isA(Long.class));
    doNothing().when(projectRepository).deleteProjectFilesAndModules(isA(Long.class));
    doNothing().when(projectRepository).deleteProjectCommits(isA(Long.class));
    doNothing().when(projectRepository).deleteProjectMetrics(isA(Long.class));
    doNothing().when(projectRepository).deleteProjectConfiguration(isA(Long.class));

    Project testProject = new Project();
    testProject.setId(1L);
    deleteProjectAdapter.delete(testProject.getId());

    verify(projectRepository, times(1)).deleteProjectFindings(anyLong());
    verify(projectRepository, times(1)).deleteProjectFilesAndModules(anyLong());
    verify(projectRepository, times(1)).deleteProjectCommits(anyLong());
    verify(projectRepository, times(1)).deleteProjectConfiguration(anyLong());
    verify(projectRepository, times(1)).deleteProjectMetrics(anyLong());
  }
}
