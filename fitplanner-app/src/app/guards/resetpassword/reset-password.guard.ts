import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { AuthenticationService } from 'src/app/service/authentication.service';

export const resetPasswordGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthenticationService);
  const router = inject(Router);
  const toastr = inject(ToastrService);

  const email = route.queryParams['email'];
  const token = route.queryParams['token'];

  if(email && token && !authService.isLoggedIn()) {
    return new Observable<boolean>((observer) => {
      authService.validateResetPasswordToken(token).subscribe(
      {
        next: () => {
          observer.next(true);
          observer.complete();
        },
        error: () => {
          router.navigate(['/login']);
          toastr.error("This link has expired.", "Error");
          observer.next(false);
          observer.complete();
        }
      });
    });
  } else {
    router.navigate(['/']);
    return false;
  }
};
