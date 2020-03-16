import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ControlPanelComponent} from './control-panel.component';
import {ActionsSubject, ReducerManager, ScannedActionsSubject, StateObservable, Store} from '@ngrx/store';
import {FocusService} from '../service/focus.service';
import {HttpClientModule} from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {AppEffects} from '../shared/effects';
import {Actions} from '@ngrx/effects';
import {RouterTestingModule} from '@angular/router/testing';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';

describe('ControlPanelComponent', () => {
  let component: ControlPanelComponent;
  let fixture: ComponentFixture<TestComponentWrapperComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        HttpClientTestingModule,
        RouterTestingModule
      ],
      declarations: [
        ControlPanelComponent
      ],
      providers: [
        Store,
        {provide: StateObservable},
        {provide: ReducerManager},
        Actions,
        ActionsSubject,
        AppEffects,
        {provide: FocusService},
        ScannedActionsSubject
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ControlPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

@Component({
  selector: 'app-test-component-wrapper',
  template: '<app-filter [activeFilter]="activeFilter"></app-filter>'
})
class TestComponentWrapperComponent {
  activeFilter: IFilter = {
    unmodified: false,
    modified: false,
    deleted: false,
    added: false,
    renamed: false,
  };
}
