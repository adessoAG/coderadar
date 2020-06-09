import {AfterViewInit, Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ProjectService} from '../../service/project.service';
import {FORBIDDEN} from 'http-status-codes';
import {UserService} from '../../service/user.service';
import {DependencyBaseComponent} from '../dependency-base/dependency-base.component';

@Component({
  selector: 'app-tree-root',
  templateUrl: './dependency-root.component.html',
  styleUrls: ['./dependency-root.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DependencyRootComponent extends DependencyBaseComponent implements OnInit, AfterViewInit {

  constructor(router: Router, userService: UserService, projectService: ProjectService, private route: ActivatedRoute) {
    super();
    this.projectService = projectService;
    this.userService = userService;
    this.router = router;
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.projectId = params.projectId;
      this.commitName = params.commitName;
      this.getProject();
    });
  }

  ngAfterViewInit(): void {
    this.getData();
  }

  getData(): void {
    this.projectService.getDependencyTree(this.projectId, this.commitName).then(response => {
      this.node = response.body;
      console.log('got data');
      this.svg = document.getElementById('3svg');
      this.checkDown = this.checkUp = true;
      console.log('wait for rendering');
      setTimeout(() => this.draw(() => this.loadDependencies(this.node)), 500);
    })
      .catch(e => {
        if (e.status && e.status === FORBIDDEN) {
          this.userService.refresh(() => this.getData());
        }
      });
  }
}
