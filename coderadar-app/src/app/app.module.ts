import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AppComponent} from './app.component';
import {LoginComponent} from './view/login/login.component';
import {RegisterComponent} from './view/register/register.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AddProjectComponent} from './view/add-project/add-project.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FlexLayoutModule} from '@angular/flex-layout';
import {MainDashboardComponent} from './view/main-dashboard/main-dashboard.component';
import {LayoutModule} from '@angular/cdk/layout';
import {AuthInterceptor} from './auth.interceptor';
import {ConfigureProjectComponent} from './view/configure-project/configure-project.component';
import {EditProjectComponent} from './view/edit-project/edit-project.component';
import {HeaderComponent} from './view/header/header.component';
import {FooterComponent} from './view/footer/footer.component';
import {UserSettingsComponent} from './view/user-settings/user-settings.component';
import {ProjectDashboardComponent} from './view/project-dashboard/project-dashboard.component';
import {ViewCommitComponent} from './view/view-commit/view-commit.component';
import {SatPopoverModule} from '@ncstate/sat-popover';
import {
  MatButtonModule,
  MatCardModule,
  MatCheckboxModule,
  MatExpansionModule,
  MatFormFieldModule,
  MatGridListModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatPaginatorModule,
  MatSidenavModule,
  MatToolbarModule
} from '@angular/material';
import {ControlPanelModule} from './city-map/control-panel/control-panel.module';
import {VisualizationModule} from './city-map/visualization/visualization.module';
import {getReducers, REDUCER_TOKEN} from './city-map/shared/reducers';
import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {AppEffects} from './city-map/shared/effects';
import {FocusService} from './city-map/service/focus.service';
import {TooltipService} from './city-map/service/tooltip.service';
import {ComparisonPanelService} from './city-map/service/comparison-panel.service';
import {environment} from '../environments/environment';
import {CityViewComponent} from './view/city-view/city-view.component';
import {CityViewHeaderComponent} from './view/city-view/city-view-header/city-view-header.component';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {SpeedDialComponent} from './view/components/speed-dial/speed-dial.component';

const appRoutes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'dashboard', component: MainDashboardComponent},
  {path: 'user-settings', component: UserSettingsComponent},
  {path: 'add-project', component: AddProjectComponent},
  {path: 'project-configure/:id', component: ConfigureProjectComponent},
  {path: 'city/:id', component: CityViewComponent},
  {path: 'project-edit/:id', component: EditProjectComponent},
  {path: 'project/:id', component: ProjectDashboardComponent},
  {path: 'project/:id/:name', component: ViewCommitComponent},
  {path: '', redirectTo: '/dashboard', pathMatch: 'full'}
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    AddProjectComponent,
    MainDashboardComponent,
    ConfigureProjectComponent,
    EditProjectComponent,
    HeaderComponent,
    FooterComponent,
    UserSettingsComponent,
    ProjectDashboardComponent,
    ViewCommitComponent,
    CityViewComponent,
    CityViewHeaderComponent,
    SpeedDialComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(appRoutes),
    BrowserAnimationsModule,
    BrowserModule,
    FontAwesomeModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatGridListModule,
    MatMenuModule,
    MatListModule,
    MatIconModule,
    RouterModule,
    LayoutModule,
    MatToolbarModule,
    MatSidenavModule,
    MatCheckboxModule,
    MatExpansionModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ControlPanelModule,
    VisualizationModule,
    StoreModule.forRoot(REDUCER_TOKEN),
    EffectsModule.forRoot([AppEffects]),
    environment.production ? [] : StoreDevtoolsModule.instrument({maxAge: 50}),
    MatPaginatorModule,
    SatPopoverModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    FocusService,
    TooltipService,
    ComparisonPanelService,
    {
      provide: REDUCER_TOKEN,
      useFactory: getReducers,
    }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
