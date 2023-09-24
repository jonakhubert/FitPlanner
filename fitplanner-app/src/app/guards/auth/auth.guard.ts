import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthenticationService } from '../../service/authentication.service';
import { Observable } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthenticationService);
  const router = inject(Router);

  if(!authService.isLoggedIn()) {
    router.navigate(['login']);
    return false;
  }

  if(authService.isLoggedIn()) {
    return new Observable<boolean>((observer) => {
      authService.authorize().subscribe(
      {
        next: () => {
          observer.next(true);
          observer.complete();
        },
        error: () => {
          router.navigate(['/login']);
          observer.next(false);
          observer.complete();
        }
      });
    });
  }

  return authService.isLoggedIn();
};
