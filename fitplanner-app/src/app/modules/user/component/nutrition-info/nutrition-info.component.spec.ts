import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NutritionInfoComponent } from './nutrition-info.component';

describe('NutritionInfoComponent', () => {
  let component: NutritionInfoComponent;
  let fixture: ComponentFixture<NutritionInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NutritionInfoComponent]
    });
    fixture = TestBed.createComponent(NutritionInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
