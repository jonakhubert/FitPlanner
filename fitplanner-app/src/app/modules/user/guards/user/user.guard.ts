import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { AuthenticationService } from 'src/app/service/authentication.service';

export const userGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthenticationService);
  const router = inject(Router);
  const toastr = inject(ToastrService);

  return new Observable<boolean>((observer) => {
    authService.authorize().subscribe(
    {
      next: () => {
        observer.next(true);
        observer.complete();
      },
      error: () => {
        router.navigate(['/login']);
        toastr.info("Your session has expired.", "Info");
        observer.next(false);
        observer.complete();
      }
    });
  });
};
