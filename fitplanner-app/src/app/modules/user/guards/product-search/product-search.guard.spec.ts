import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { productSearchGuard } from './product-search.guard';

describe('productSearchGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => productSearchGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
