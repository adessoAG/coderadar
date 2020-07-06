import {Component, Inject, OnInit} from '@angular/core';
import {Project} from '../../model/project';
import {ProjectService} from '../../service/project.service';
import {Router} from '@angular/router';
import {UserService} from '../../service/user.service';
import {FORBIDDEN, UNPROCESSABLE_ENTITY} from 'http-status-codes';
import {Title} from '@angular/platform-browser';
import {AppComponent} from '../../app.component';
import {MatDialog, MatSnackBar} from '@angular/material';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Team} from "../../model/team";
import {TeamService} from "../../service/team.service";
import {HttpResponse} from "@angular/common/http";
import {ProjectRole} from "../../model/project-role";
import {FormControl} from "@angular/forms";
import {ProjectWithRoles} from "../../model/project-with-roles";

@Component({
  selector: 'app-main-dashboard',
  templateUrl: './main-dashboard.component.html',
  styleUrls: ['./main-dashboard.component.scss']
})
export class MainDashboardComponent implements OnInit {

  projects: ProjectWithRoles[] = [];
  teams: Team[] = [];

  dialogRef: MatDialogRef<ConfirmDeleteProjectDialogComponent>;
  teamDialogRef: MatDialogRef<AddProjectToTeamDialogComponent>;

  appComponent = AppComponent;
  waiting = false;
  selectedTeam: Team;

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
    this.projectService.deleteProject(project.project.id)
      .then(() => {
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
        }
      });
  }

  openProjectDeletionDialog(projectToBeDeleted: Project) {
    this.dialogRef = this.dialog.open(ConfirmDeleteProjectDialogComponent, {
      data: {
        project: projectToBeDeleted
      }
    });

    this.dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteProject(result);
      }
    });
  }

  openAddToTeamDialog(project: Project): void {
    const dialogRef = this.dialog.open(AddProjectToTeamDialogComponent, {
      width: '300px',
      data: {teams: this.teams, project: project}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        for(let team of result.teams) { //TODO: Check if project is already in team
          this.teamService.addTeamToProject(project.id, team.id, result.role);
        }
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
    if(this.selectedTeam == undefined) {
      promise = this.projectService.listProjectsForUser(UserService.getLoggedInUser().userId);
    } else {
      //promise = this.teamService.listProjectsForTeam(this.selectedTeam.id);
    }
    promise
      .then(response => {
        this.projects = [];
        response.body.forEach(project => {
          console.log(project);

          const newProject = new Project(project.project);

          const projectWithRoles = new ProjectWithRoles();
        projectWithRoles.project = newProject;
        projectWithRoles.roles = project.roles;
          this.projects.push(projectWithRoles);
        });
                         this.waiting = false;
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
      if (e.status && e.status === FORBIDDEN) { //TODO: UNAUTHORIZED
        this.userService.refresh(() => this.getTeams());
      }
    });
  }

  startAnalysis(id: number) {
    this.projectService.startAnalyzingJob(id, 'master').then(() => {
      this.openSnackBar('Analysis started!', '🞩');
    }).catch(error => {
      if (error.status && error.status === FORBIDDEN) {
        this.userService.refresh(() => this.projectService.startAnalyzingJob(id, 'master'));
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
    }).catch(error => {
      if (error.status && error.status === FORBIDDEN) {
        this.userService.refresh(() => this.projectService.stopAnalyzingJob(id));
      } else if (error.status && error.status === UNPROCESSABLE_ENTITY) {
        this.openSnackBar('Analysis stopped!', '🞩');
      }
    });
  }
}

class DeleteProjectDialogData {
  project: Project;
}

@Component({
  selector: 'app-delete-project-dialog',
  templateUrl: 'delete-project-dialog.html'
})
export class ConfirmDeleteProjectDialogComponent{

  constructor(
    public dialogRef: MatDialogRef<ConfirmDeleteProjectDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DeleteProjectDialogData
  ) {

  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}


class AddProjectToTeamDialogData {
  project: Project;
  teams: Team[];
  role: ProjectRole;
}

@Component({
  selector: 'app-add-project-to-team-dialog',
  templateUrl: 'add-project-to-team-dialog.html'
})
export class AddProjectToTeamDialogComponent {

  selectedTeams: FormControl = new FormControl();

  constructor(
    public dialogRef: MatDialogRef<AddProjectToTeamDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: AddProjectToTeamDialogData
  ) {

  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onAddClick(): void {
    this.data.teams = this.selectedTeams.value;
  }

}
