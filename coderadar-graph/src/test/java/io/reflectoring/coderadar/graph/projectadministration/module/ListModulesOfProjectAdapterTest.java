package io.reflectoring.coderadar.graph.projectadministration.module;

import static org.mockito.Mockito.*;

import io.reflectoring.coderadar.graph.projectadministration.domain.ModuleEntity;
import io.reflectoring.coderadar.graph.projectadministration.module.adapter.ListModulesOfProjectAdapter;
import io.reflectoring.coderadar.graph.projectadministration.module.repository.ModuleRepository;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.ProjectRepository;
import io.reflectoring.coderadar.projectadministration.domain.Module;
import java.util.LinkedList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("List modules of project")
class ListModulesOfProjectAdapterTest {
  private ModuleRepository moduleRepository = mock(ModuleRepository.class);

  private ProjectRepository projectRepository = mock(ProjectRepository.class);

  private ListModulesOfProjectAdapter listModulesOfProjectAdapter;

  @BeforeEach
  void setUp() {
    listModulesOfProjectAdapter = new ListModulesOfProjectAdapter(moduleRepository);
  }

  @Test
  @DisplayName("Should return empty list when no modules in the project exist")
  void shouldReturnEmptyListWhenNoModulesInTheProjectExist() {
    when(moduleRepository.findModulesInProject(1L)).thenReturn(new LinkedList<>());

    Iterable<Module> modules = listModulesOfProjectAdapter.listModules(1L);
    verify(moduleRepository, times(1)).findModulesInProject(1L);
    Assertions.assertThat(modules).hasSize(0);
  }

  @Test
  @DisplayName("Should return list with size of one when one module in the project exists")
  void shouldReturnListWithSizeOfOneWhenOneModuleInTheProjectExists() {
    LinkedList<ModuleEntity> mockedItem = new LinkedList<>();
    mockedItem.add(new ModuleEntity());
    when(projectRepository.existsById(1L)).thenReturn(true);
    when(moduleRepository.findModulesInProject(1L)).thenReturn(mockedItem);

    Iterable<Module> modules = listModulesOfProjectAdapter.listModules(1L);
    verify(moduleRepository, times(1)).findModulesInProject(1L);
    Assertions.assertThat(modules).hasSize(1);
  }

  @Test
  @DisplayName("Should return list with size of two when two modules in the project exist")
  void shouldReturnListWithSizeOfTwoWhenTwoModulesInTheProjectExist() {
    LinkedList<ModuleEntity> mockedItem = new LinkedList<>();
    mockedItem.add(new ModuleEntity());
    mockedItem.add(new ModuleEntity());
    when(projectRepository.existsById(1L)).thenReturn(true);
    when(moduleRepository.findModulesInProject(1L)).thenReturn(mockedItem);

    Iterable<Module> modules = listModulesOfProjectAdapter.listModules(1L);
    verify(moduleRepository, times(1)).findModulesInProject(1L);
    Assertions.assertThat(modules).hasSize(2);
  }
}
