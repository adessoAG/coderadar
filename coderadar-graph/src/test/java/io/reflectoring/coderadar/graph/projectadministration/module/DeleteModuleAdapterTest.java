package io.reflectoring.coderadar.graph.projectadministration.module;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import io.reflectoring.coderadar.graph.projectadministration.domain.ModuleEntity;
import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.module.repository.DeleteModuleRepository;
import io.reflectoring.coderadar.graph.projectadministration.module.service.DeleteModuleAdapter;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.CreateProjectRepository;
import io.reflectoring.coderadar.projectadministration.domain.Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Delete module")
class DeleteModuleAdapterTest {
  private DeleteModuleRepository deleteModuleRepository = mock(DeleteModuleRepository.class);
  private CreateProjectRepository createProjectRepository = mock(CreateProjectRepository.class);

  private DeleteModuleAdapter deleteModuleAdapter;
  private ModuleEntity moduleEntity;

  @BeforeEach
  void setUp() {
    ProjectEntity projectEntity = new ProjectEntity();
    projectEntity.setId(2L);
    moduleEntity = new ModuleEntity();
    moduleEntity.setId(1L);
    moduleEntity.setProject(projectEntity);
    deleteModuleAdapter =
        new DeleteModuleAdapter(
            deleteModuleRepository, createProjectRepository, getProjectRepository, taskExecutor);
  }

  @Test
  @DisplayName("Should delete module when passing a valid module entity")
  void shouldDeleteModuleWhenPassingAValidModuleEntity() {
    doNothing().when(deleteModuleRepository).delete(isA(ModuleEntity.class));
    when(deleteModuleRepository.findById(anyLong()))
        .thenReturn(java.util.Optional.of(moduleEntity));
    Module module = new Module();
    module.setId(1L);
    deleteModuleAdapter.delete(module);
    verify(deleteModuleRepository, times(1)).deleteById(1L);
  }

  @Test
  @DisplayName("Should delete module when passing a valid module id")
  void shouldDeleteModuleWhenPassingAValidModuleId() {
    doNothing().when(deleteModuleRepository).deleteById(isA(Long.class));
    when(deleteModuleRepository.findById(anyLong()))
        .thenReturn(java.util.Optional.of(moduleEntity));
    deleteModuleAdapter.delete(1L);
    verify(deleteModuleRepository, times(1)).deleteById(any(Long.class));
  }
}
