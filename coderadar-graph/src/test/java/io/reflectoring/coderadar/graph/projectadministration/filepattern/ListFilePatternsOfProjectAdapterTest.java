package io.reflectoring.coderadar.graph.projectadministration.filepattern;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.reflectoring.coderadar.domain.FilePattern;
import io.reflectoring.coderadar.graph.projectadministration.filepattern.adapter.ListFilePatternsOfProjectAdapter;
import io.reflectoring.coderadar.graph.projectadministration.filepattern.repository.FilePatternRepository;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.ProjectRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("List file patterns of project")
class ListFilePatternsOfProjectAdapterTest {

  private FilePatternRepository filePatternRepository = mock(FilePatternRepository.class);
  private ProjectRepository projectRepository = mock(ProjectRepository.class);

  @Test
  @DisplayName("Should return list of file patterns when passing valid argument")
  void shouldReturnListOfFilePatternsWhenPassingValidArgument() {
    ListFilePatternsOfProjectAdapter listFilePatternsOfProjectAdapter =
        new ListFilePatternsOfProjectAdapter(filePatternRepository);

    when(projectRepository.existsById(1L)).thenReturn(true);

    List<FilePattern> returnedList = listFilePatternsOfProjectAdapter.listFilePatterns(1L);
    Assertions.assertThat(returnedList).isNotNull();
  }
}
