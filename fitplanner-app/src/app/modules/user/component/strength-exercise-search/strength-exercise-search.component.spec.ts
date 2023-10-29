import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StrengthExerciseSearchComponent } from './strength-exercise-search.component';

describe('StrengthExerciseSearchComponent', () => {
  let component: StrengthExerciseSearchComponent;
  let fixture: ComponentFixture<StrengthExerciseSearchComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StrengthExerciseSearchComponent]
    });
    fixture = TestBed.createComponent(StrengthExerciseSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
