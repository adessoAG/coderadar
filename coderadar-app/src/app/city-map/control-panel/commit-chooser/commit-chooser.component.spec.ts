import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CommitChooserComponent} from './commit-chooser.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatAutocompleteModule, MatFormFieldModule, MatInputModule} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AutosuggestWrapperComponent} from '../../autosuggest-wrapper/autosuggest-wrapper.component';

describe('CommitChooserComponent', () => {
  let component: CommitChooserComponent;
  let fixture: ComponentFixture<CommitChooserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({/*
          imports: [ReactiveFormsModule, MatInputModule, MatAutocompleteModule, MatFormFieldModule, FormsModule,
            BrowserAnimationsModule],*/
      imports: [ReactiveFormsModule, MatAutocompleteModule, MatFormFieldModule, FormsModule, MatInputModule,
        BrowserAnimationsModule],
      declarations: [CommitChooserComponent, AutosuggestWrapperComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommitChooserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
