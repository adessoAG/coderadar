package io.reflectoring.coderadar.graph.projectadministration.module;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import io.reflectoring.coderadar.core.projectadministration.domain.Module;
import io.reflectoring.coderadar.graph.projectadministration.module.repository.DeleteModuleRepository;
import io.reflectoring.coderadar.graph.projectadministration.module.service.DeleteModuleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DeleteModuleServiceTest {
  @Mock private DeleteModuleRepository deleteModuleRepository;

  @InjectMocks private DeleteModuleService deleteModuleService;

  @Test
  public void withModuleEntityShouldCallDeleteModuleMethodOfRepository() {
    doNothing().when(deleteModuleRepository).delete(isA(Module.class));
    deleteModuleService.delete(new Module());
    verify(deleteModuleRepository, times(1)).delete(any(Module.class));
  }

  @Test
  public void withModuleIdShouldCallDeleteModuleMethodOfRepository() {
    doNothing().when(deleteModuleRepository).deleteById(isA(Long.class));
    deleteModuleService.delete(1L);
    verify(deleteModuleRepository, times(1)).deleteById(any(Long.class));
  }
}
