import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { exerciseSearchGuard } from './exercise-search.guard';

describe('exerciseSearchGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => exerciseSearchGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
