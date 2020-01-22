package io.reflectoring.coderadar.graph.projectadministration.project;

import static org.mockito.Mockito.*;

import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.project.adapter.ListProjectsAdapter;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.ProjectRepository;
import io.reflectoring.coderadar.projectadministration.domain.Project;
import java.util.LinkedList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("List projects")
class ListProjectsAdapterTest {
  private ProjectRepository projectRepository = mock(ProjectRepository.class);

  private ListProjectsAdapter listProjectsAdapter;

  @BeforeEach
  void setUp() {
    listProjectsAdapter = new ListProjectsAdapter(projectRepository);
  }

  @Test
  @DisplayName("Should return empty list when no projects exist")
  void shouldReturnEmptyListWhenNoProjectsExist() {
    when(projectRepository.findAll()).thenReturn(new LinkedList<>());

    Iterable<Project> projects = listProjectsAdapter.getProjects();
    verify(projectRepository, times(1)).findAll();
    Assertions.assertThat(projects).hasSize(0);
  }

  @Test
  @DisplayName("Should return list with size of one when one project exists")
  void shouldReturnListWithSizeOfOneWhenOneProjectExists() {
    LinkedList<ProjectEntity> mockedItem = new LinkedList<>();
    mockedItem.add(new ProjectEntity());
    when(projectRepository.findAll()).thenReturn(mockedItem);

    Iterable<Project> projects = listProjectsAdapter.getProjects();
    verify(projectRepository, times(1)).findAll();
    Assertions.assertThat(projects).hasSize(1);
  }

  @Test
  @DisplayName("Should return list with size of two when two projects exist")
  void shouldReturnListWithSizeOfTwoWhenTwoProjectsExist() {
    LinkedList<ProjectEntity> mockedItem = new LinkedList<>();
    mockedItem.add(new ProjectEntity());
    mockedItem.add(new ProjectEntity());
    when(projectRepository.findAll()).thenReturn(mockedItem);

    Iterable<Project> projects = listProjectsAdapter.getProjects();
    verify(projectRepository, times(1)).findAll();
    Assertions.assertThat(projects).hasSize(2);
  }
}
