import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardioExerciseSearchComponent } from './cardio-exercise-search.component';

describe('CardioExerciseSearchComponent', () => {
  let component: CardioExerciseSearchComponent;
  let fixture: ComponentFixture<CardioExerciseSearchComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CardioExerciseSearchComponent]
    });
    fixture = TestBed.createComponent(CardioExerciseSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
