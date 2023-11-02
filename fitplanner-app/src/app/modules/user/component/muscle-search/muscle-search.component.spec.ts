import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MuscleSearchComponent } from './muscle-search.component';

describe('MuscleSearchComponent', () => {
  let component: MuscleSearchComponent;
  let fixture: ComponentFixture<MuscleSearchComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MuscleSearchComponent]
    });
    fixture = TestBed.createComponent(MuscleSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
