package io.reflectoring.coderadar.projectadministration.service.project;

import static io.reflectoring.coderadar.projectadministration.service.project.CreateProjectService.getProjectDateRange;

import io.reflectoring.coderadar.CoderadarConfigurationProperties;
import io.reflectoring.coderadar.CoderadarConstants;
import io.reflectoring.coderadar.analyzer.service.AnalyzingService;
import io.reflectoring.coderadar.projectadministration.ProjectNotFoundException;
import io.reflectoring.coderadar.projectadministration.domain.Branch;
import io.reflectoring.coderadar.projectadministration.domain.Commit;
import io.reflectoring.coderadar.projectadministration.domain.Module;
import io.reflectoring.coderadar.projectadministration.domain.Project;
import io.reflectoring.coderadar.projectadministration.port.driven.analyzer.UpdateCommitsPort;
import io.reflectoring.coderadar.projectadministration.port.driven.branch.DeleteBranchPort;
import io.reflectoring.coderadar.projectadministration.port.driven.module.CreateModulePort;
import io.reflectoring.coderadar.projectadministration.port.driven.module.DeleteModulePort;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.projectadministration.port.driven.project.ListProjectsPort;
import io.reflectoring.coderadar.projectadministration.port.driven.project.ProjectStatusPort;
import io.reflectoring.coderadar.projectadministration.port.driver.module.get.ListModulesOfProjectUseCase;
import io.reflectoring.coderadar.useradministration.service.security.PasswordUtil;
import io.reflectoring.coderadar.vcs.port.driver.ExtractProjectCommitsUseCase;
import io.reflectoring.coderadar.vcs.port.driver.update.UpdateLocalRepositoryUseCase;
import io.reflectoring.coderadar.vcs.port.driver.update.UpdateRepositoryCommand;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScanProjectScheduler {

  private final UpdateLocalRepositoryUseCase updateLocalRepositoryUseCase;
  private final CoderadarConfigurationProperties coderadarConfigurationProperties;
  private final ExtractProjectCommitsUseCase extractProjectCommitsUseCase;
  private final ListProjectsPort listProjectsPort;
  private final ProjectStatusPort projectStatusPort;
  private final TaskScheduler taskScheduler;
  private final GetProjectPort getProjectPort;
  private final ListModulesOfProjectUseCase listModulesOfProjectUseCase;
  private final CreateModulePort createModulePort;
  private final UpdateCommitsPort updateCommitsPort;
  private final DeleteModulePort deleteModulePort;
  private final TaskExecutor taskExecutor;
  private final DeleteBranchPort deleteBranchPort;
  private final AnalyzingService analyzingService;

  private static final Logger logger = LoggerFactory.getLogger(ScanProjectScheduler.class);

  private final Map<Long, ScheduledFuture<?>> tasks = new HashMap<>();

  /** Starts the scheduleCheckTask tasks upon application start */
  @EventListener({ContextRefreshedEvent.class})
  public void onApplicationEvent() {
    for (Project project : listProjectsPort.getProjects()) {
      projectStatusPort.setBeingProcessed(project.getId(), false);
    }
    taskScheduler.scheduleAtFixedRate(
        this::scheduleCheckTask,
        coderadarConfigurationProperties.getScanIntervalInSeconds() * 1000L);
  }

  /** Starts update tasks for all projects that don't have one running already. */
  private void scheduleCheckTask() {
    for (Project project : listProjectsPort.getProjects()) {
      if (!tasks.containsKey(project.getId())) {
        scheduleUpdateTask(project.getId());
      }
    }
  }

  /**
   * Schedules an update task for the project. This will do a pull on the repository and save the
   * commits in the database.
   *
   * @param projectId the project id
   */
  private void scheduleUpdateTask(long projectId) {
    tasks.put(
        projectId,
        taskScheduler.scheduleAtFixedRate(
            () ->
                taskExecutor.execute(
                    () -> {
                      Project currentProject;
                      try {
                        currentProject = getProjectPort.get(projectId);
                      } catch (ProjectNotFoundException e) {
                        stopUpdateTask(projectId);
                        return;
                      }
                      if (!projectStatusPort.isBeingProcessed(projectId)) {
                        projectStatusPort.setBeingProcessed(projectId, true);
                        logger.info(
                            "Scanning project {} for new commits!", currentProject.getName());
                        List<String> updatedBranches = Collections.emptyList();
                        try {
                          updatedBranches = checkForNewCommits(currentProject);
                        } finally {
                          projectStatusPort.setBeingProcessed(projectId, false);
                          if (!updatedBranches.isEmpty()) {
                            analyzingService.start(projectId, updatedBranches);
                          }
                        }
                      }
                    }),
            coderadarConfigurationProperties.getScanIntervalInSeconds() * 1000L));
  }

  private void stopUpdateTask(long projectId) {
    ScheduledFuture<?> f = tasks.get(projectId);
    if (f != null) {
      f.cancel(false);
    }
    tasks.remove(projectId);
  }

  public List<String> checkForNewCommits(Project project) {
    try {
      String localDir =
          coderadarConfigurationProperties.getWorkdir() + "/projects/" + project.getWorkdirName();

      List<Branch> updatedBranches =
          updateLocalRepositoryUseCase.updateRepository(
              new UpdateRepositoryCommand()
                  .setLocalDir(localDir)
                  .setPassword(PasswordUtil.decrypt(project.getVcsPassword()))
                  .setUsername(project.getVcsUsername())
                  .setRemoteUrl(project.getVcsUrl()));

      if (!updatedBranches.isEmpty()) {
        for (Branch branch : updatedBranches) {
          if (branch.getCommitHash().equals(CoderadarConstants.ZERO_HASH)) {
            deleteBranchPort.delete(project.getId(), branch);
          }
        }

        // Check what modules where previously in the project
        List<Module> modules = listModulesOfProjectUseCase.listModules(project.getId());

        // Delete modules
        for (Module module : modules) {
          deleteModulePort.delete(module.getId(), project.getId());
        }

        List<Commit> commits =
            extractProjectCommitsUseCase.getCommits(localDir, getProjectDateRange(project));

        updateCommitsPort.updateCommits(project.getId(), commits, updatedBranches);

        // Re-create the modules
        for (Module module : modules) {
          createModulePort.createModule(module.getPath(), project.getId());
        }
      }
      return updatedBranches.stream().map(Branch::getName).collect(Collectors.toList());
    } catch (Exception e) {
      logger.error("Unable to update the project: {}", e.toString());
    }
    return Collections.emptyList();
  }
}
