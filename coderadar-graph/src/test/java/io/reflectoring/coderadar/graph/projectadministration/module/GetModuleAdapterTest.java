package io.reflectoring.coderadar.graph.projectadministration.module;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import io.reflectoring.coderadar.domain.Module;
import io.reflectoring.coderadar.graph.projectadministration.domain.ModuleEntity;
import io.reflectoring.coderadar.graph.projectadministration.module.adapter.GetModuleAdapter;
import io.reflectoring.coderadar.graph.projectadministration.module.repository.ModuleRepository;
import io.reflectoring.coderadar.projectadministration.ModuleNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Get module")
class GetModuleAdapterTest {
  private ModuleRepository moduleRepository = mock(ModuleRepository.class);

  private GetModuleAdapter getModuleAdapter;

  @BeforeEach
  void setUp() {
    getModuleAdapter = new GetModuleAdapter(moduleRepository);
  }

  @Test
  @DisplayName("Should return module as optional when a module with the passing ID exists")
  void shouldReturnModuleAsOptionalWhenAModuleWithThePassingIdExists() {
    ModuleEntity mockedItem = new ModuleEntity();
    mockedItem.setId(1L);
    when(moduleRepository.findById(anyLong(), anyInt())).thenReturn(Optional.of(mockedItem));

    Module returned = getModuleAdapter.get(1L);

    verify(moduleRepository, times(1)).findById(1L, 0);
    verifyNoMoreInteractions(moduleRepository);
    Assertions.assertNotNull(returned);
    Assertions.assertEquals(1L, returned.getId());
  }

  @Test
  @DisplayName(
      "Should return module as empty optional when a module with the passing ID doesn't exists")
  void shouldReturnModuleAsEmptyOptionalWhenAModuleWithThePassingIdDoesntExists() {
    Optional<ModuleEntity> mockedItem = Optional.empty();
    when(moduleRepository.findById(anyLong())).thenReturn(mockedItem);

    Assertions.assertThrows(ModuleNotFoundException.class, () -> getModuleAdapter.get(1L));
    verify(moduleRepository, times(1)).findById(1L, 0);
    verifyNoMoreInteractions(moduleRepository);
  }
}
