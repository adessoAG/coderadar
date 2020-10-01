import {Component, OnDestroy, OnInit} from '@angular/core';
import {Project} from '../../model/project';
import {ProjectService} from '../../service/project.service';
import {Router} from '@angular/router';
import {UserService} from '../../service/user.service';
import {FORBIDDEN, UNPROCESSABLE_ENTITY} from 'http-status-codes';
import {Title} from '@angular/platform-browser';
import {AppComponent} from '../../app.component';
import {MatDialog, MatSnackBar} from '@angular/material';
import {MatDialogRef} from '@angular/material/dialog';
import {ProjectWithRoles} from '../../model/project-with-roles';
import {Team} from '../../model/team';
import {TeamService} from '../../service/team.service';
import {HttpResponse} from '@angular/common/http';
import {DeleteProjectDialogComponent} from '../../components/delete-project-dialog/delete-project-dialog.component';
import {AddProjectToTeamDialogComponent} from '../../components/add-project-to-team-dialog/add-project-to-team-dialog.component';
import {Subscription, timer} from 'rxjs';

@Component({
  selector: 'app-main-dashboard',
  templateUrl: './main-dashboard.component.html',
  styleUrls: ['./main-dashboard.component.scss']
})
export class MainDashboardComponent implements OnInit, OnDestroy {

  projects: ProjectWithRoles[] = [];
  analysisStatuses = new Map();

  teams: Team[] = [];
  teamDialogRef: MatDialogRef<AddProjectToTeamDialogComponent>;
  dialogRef: MatDialogRef<DeleteProjectDialogComponent>;
  appComponent = AppComponent;
  waiting = false;
  selectedTeam: Team;
  deletingProject = false;
  updateStatusesTimer: Subscription;

  constructor(private snackBar: MatSnackBar, private titleService: Title, private userService: UserService,
              private router: Router, private projectService: ProjectService, private dialog: MatDialog,
              private teamService: TeamService) {
    titleService.setTitle('Coderadar - Dashboard');
  }

  ngOnInit(): void {
    this.getProjects();
    this.getTeams();
  }

  /**
   * Deletes a project from the database.
   * Only works if project is not currently being analyzed.
   * @param project The project to delete
   */
  deleteProject(project: ProjectWithRoles): void {
    this.waiting = true;
    this.deletingProject = true;
    this.projectService.deleteProject(project.project.id)
      .then(() => {
        this.waiting = false;
        this.deletingProject = false;
        const index = this.projects.indexOf(project, 0);
        if (this.projects.indexOf(project, 0) > -1) {
          this.projects.splice(index, 1);
        }
      })
      .catch(error => {
        if (error.status && error.status === FORBIDDEN) {
          this.userService.refresh(() => this.deleteProject(project));
        } else if (error.status && error.status === UNPROCESSABLE_ENTITY) {
          this.openSnackBar('Cannot delete project! Try again later!', '🞩');
          this.waiting = false;
          this.deletingProject = false;
        }
      });
  }

  openProjectDeletionDialog(projectToBeDeleted: ProjectWithRoles) {
    this.dialogRef = this.dialog.open(DeleteProjectDialogComponent, {
      data: {
        project: projectToBeDeleted.project
      }
    });

    this.dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteProject(projectToBeDeleted);
      }
    });
  }

  openAddToTeamDialog(project: Project): void {
    this.teamService.listTeamsForProject(project.id).then(value => {
      const dialogRef = this.dialog.open(AddProjectToTeamDialogComponent, {
        width: '300px',
        data: {teams: this.teams, project, teamsForProject: value.body}
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result !== undefined) {
          for (const team of result.teamsForProject) {
            if (result.teams.find(t => t.name === team.name) === undefined) {
              this.teamService.removeTeamFromProject(project.id, team.id);
            }
          }
          for (const team of result.teams) {
            this.teamService.addTeamToProject(project.id, team.id, result.role);
          }
        }
      });
    }).catch(e => {
      if (e.status && e.status === FORBIDDEN) {
        this.userService.refresh(() => this.openAddToTeamDialog(project));
      }
    });

  }

  /**
   * Gets all projects from the project service and constructs a new array of Project objects
   * from the returned JSON. Sends a refresh token if access is denied.
   */
  getProjects(): void {
    this.waiting = true;
    let promise: Promise<HttpResponse<ProjectWithRoles[]>>;
    if (this.selectedTeam === undefined) {
      promise = this.projectService.listProjectsForUser(UserService.getLoggedInUser().userId);
    } else {
      promise = this.teamService.listProjectsForTeamWithRolesForUser(this.selectedTeam.id,
        UserService.getLoggedInUser().userId);
    }
    promise
      .then(response => {
        this.projects = [];
        response.body.forEach(project => {
          const newProject = new Project(project.project);
          const projectWithRoles = new ProjectWithRoles();
          projectWithRoles.project = newProject;
          projectWithRoles.roles = project.roles;
          this.projects.push(projectWithRoles);
          this.getAnalyzingStatus(project.project.id);
        });
        this.waiting = false;
        this.updateStatusesTimer = timer(4000, 8000).subscribe(() => {
          this.projects.forEach(value => this.getAnalyzingStatus(value.project.id));
        });
        }
      )
      .catch(e => {
        if (e.status && e.status === FORBIDDEN) {
          this.userService.refresh(() => this.getProjects());
        }
      });
  }

  private getTeams() {
    this.teamService.listTeamsForUser(UserService.getLoggedInUser().userId).then(value =>
      this.teams = value.body
    ) .catch(e => {
      if (e.status && e.status === FORBIDDEN) {
        this.userService.refresh(() => this.getTeams());
      }
    });
  }

  startAnalysis(id: number) {
    this.projectService.startAnalyzingJob(id).then(() => {
      this.openSnackBar('Analysis started!', '🞩');
      this.analysisStatuses.set(id, true);
    }).catch(error => {
      if (error.status && error.status === FORBIDDEN) {
        this.userService.refresh(() => this.projectService.startAnalyzingJob(id));
      } else if (error.status && error.status === UNPROCESSABLE_ENTITY) {
        if (error.error.errorMessage === 'Cannot analyze project without analyzers') {
          this.openSnackBar('Cannot analyze, no analyzers configured for this project!', '🞩');
        } else if (error.error.errorMessage === 'Cannot analyze project without file patterns') {
          this.openSnackBar('Cannot analyze, no file patterns configured for this project!', '🞩');
        } else {
          this.openSnackBar('Analysis cannot be started! Try again later!', '🞩');
        }
      }
    });
  }

  resetAnalysis(id: number) {
    this.projectService.resetAnalysis(id).then(() => {
      this.openSnackBar('Analysis results deleted!', '🞩');
    }).catch(error => {
      if (error.status && error.status === FORBIDDEN) {
        this.userService.refresh(() => this.projectService.resetAnalysis(id));
      } else if (error.status && error.status === UNPROCESSABLE_ENTITY) {
        this.openSnackBar('Analysis results cannot be deleted! Try again later!', '🞩');
      }
    });
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 4000,
    });
  }

  stopAnalysis(id: number) {
    this.projectService.stopAnalyzingJob(id).then(() => {
      this.openSnackBar('Analysis stopped!', '🞩');
      this.analysisStatuses.set(id, false);
    }).catch(error => {
      if (error.status && error.status === FORBIDDEN) {
        this.userService.refresh(() => this.projectService.stopAnalyzingJob(id));
      } else if (error.status && error.status === UNPROCESSABLE_ENTITY) {
        this.openSnackBar('Analysis stopped!', '🞩');
      }
    });
  }

  private getAnalyzingStatus(id: number) {
    this.projectService.getAnalyzingStatus(id).then(value => {
      this.analysisStatuses.set(id, value.body.status);
    }).catch(error => {
      if (error.status && error.status === FORBIDDEN) {
        this.userService.refresh(() => this.getAnalyzingStatus(id));
      }
    });
  }

  ngOnDestroy(): void {
    this.updateStatusesTimer.unsubscribe();
  }
}
