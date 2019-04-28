package io.reflectoring.coderadar.graph.projectadministration.module;

import static org.mockito.Mockito.*;

import io.reflectoring.coderadar.core.projectadministration.domain.Module;
import io.reflectoring.coderadar.core.projectadministration.domain.Project;
import io.reflectoring.coderadar.graph.exception.InvalidArgumentException;
import io.reflectoring.coderadar.graph.exception.ProjectNotFoundException;
import io.reflectoring.coderadar.graph.projectadministration.module.repository.ListModulesOfProjectRepository;
import io.reflectoring.coderadar.graph.projectadministration.module.service.ListModulesOfProjectService;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.GetProjectRepository;
import java.util.LinkedList;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ListModulesOfProjectServiceTest {
  @Mock private ListModulesOfProjectRepository listModulesOfProjectRepository;

  @Mock private GetProjectRepository getProjectRepository;

  @InjectMocks private ListModulesOfProjectService listModulesOfProjectService;

  @Test
  public void withInvalidArgumentShouldThrowInvalidArgumentException() {
    org.junit.jupiter.api.Assertions.assertThrows(
        InvalidArgumentException.class, () -> listModulesOfProjectService.listModules(null));
  }

  @Test
  public void withNoPersistedProjectShouldThrowProjectNotFoundException() {
    org.junit.jupiter.api.Assertions.assertThrows(
        ProjectNotFoundException.class, () -> listModulesOfProjectService.listModules(1L));
  }

  @Test
  public void withNoModulesShouldReturnEmptyList() {
    Project mockedProject = new Project();
    when(getProjectRepository.findById(1L)).thenReturn(java.util.Optional.of(mockedProject));
    when(listModulesOfProjectRepository.findByProject_Id(1L)).thenReturn(new LinkedList<>());

    Iterable<Module> modules = listModulesOfProjectService.listModules(1L);
    verify(listModulesOfProjectRepository, times(1)).findByProject_Id(1L);
    Assertions.assertThat(modules).hasSize(0);
  }

  @Test
  public void withOneModuleShouldReturnListWithSizeOfOne() {
    LinkedList<Module> mockedItem = new LinkedList<>();
    mockedItem.add(new Module());
    Project mockedProject = new Project();
    when(getProjectRepository.findById(1L)).thenReturn(java.util.Optional.of(mockedProject));
    when(listModulesOfProjectRepository.findByProject_Id(1L)).thenReturn(mockedItem);

    Iterable<Module> modules = listModulesOfProjectService.listModules(1L);
    verify(listModulesOfProjectRepository, times(1)).findByProject_Id(1L);
    Assertions.assertThat(modules).hasSize(1);
  }

  @Test
  public void withTwoModulesShouldReturnListWithSizeOf() {
    LinkedList<Module> mockedItem = new LinkedList<>();
    mockedItem.add(new Module());
    mockedItem.add(new Module());
    Project mockedProject = new Project();
    when(getProjectRepository.findById(1L)).thenReturn(java.util.Optional.of(mockedProject));
    when(listModulesOfProjectRepository.findByProject_Id(1L)).thenReturn(mockedItem);

    Iterable<Module> modules = listModulesOfProjectService.listModules(1L);
    verify(listModulesOfProjectRepository, times(1)).findByProject_Id(1L);
    Assertions.assertThat(modules).hasSize(2);
  }
}
