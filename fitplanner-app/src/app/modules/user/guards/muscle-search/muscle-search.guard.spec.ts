import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { muscleSearchGuard } from './muscle-search.guard';

describe('muscleSearchGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => muscleSearchGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
