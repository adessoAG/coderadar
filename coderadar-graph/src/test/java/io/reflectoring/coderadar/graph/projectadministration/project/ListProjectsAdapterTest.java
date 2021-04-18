package io.reflectoring.coderadar.graph.projectadministration.project;

import static org.mockito.Mockito.*;

import io.reflectoring.coderadar.domain.Project;
import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.project.adapter.ListProjectsAdapter;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.ProjectRepository;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("List projects")
class ListProjectsAdapterTest {
  private final ProjectRepository projectRepository = mock(ProjectRepository.class);

  private ListProjectsAdapter listProjectsAdapter;

  @BeforeEach
  void setUp() {
    listProjectsAdapter = new ListProjectsAdapter(projectRepository);
  }

  @Test
  @DisplayName("Should return empty list when no projects exist")
  void shouldReturnEmptyListWhenNoProjectsExist() {
    when(projectRepository.findAll()).thenReturn(new ArrayList<>());

    List<Project> projects = listProjectsAdapter.getProjects();
    verify(projectRepository, times(1)).findAll();
    Assertions.assertThat(projects).isEmpty();
  }

  @Test
  @DisplayName("Should return list with size of one when one project exists")
  void shouldReturnListWithSizeOfOneWhenOneProjectExists() {
    List<ProjectEntity> mockedItem = new ArrayList<>();
    mockedItem.add(new ProjectEntity().setId(1L));
    when(projectRepository.findAll()).thenReturn(mockedItem);

    List<Project> projects = listProjectsAdapter.getProjects();
    verify(projectRepository, times(1)).findAll();
    Assertions.assertThat(projects).hasSize(1);
  }

  @Test
  @DisplayName("Should return list with size of two when two projects exist")
  void shouldReturnListWithSizeOfTwoWhenTwoProjectsExist() {
    List<ProjectEntity> mockedItem = new ArrayList<>();
    mockedItem.add(new ProjectEntity().setId(1L));
    mockedItem.add(new ProjectEntity().setId(1L));
    when(projectRepository.findAll()).thenReturn(mockedItem);

    List<Project> projects = listProjectsAdapter.getProjects();
    verify(projectRepository, times(1)).findAll();
    Assertions.assertThat(projects).hasSize(2);
  }
}
