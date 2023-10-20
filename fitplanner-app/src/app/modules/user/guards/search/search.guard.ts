import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const searchGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const dateParam = route.queryParamMap.get('date');
  const dateFormatRegex = /^\d{4}-\d{2}-\d{2}$/;

    if(dateParam && dateParam.match(dateFormatRegex))
      return true;

    router.navigate(['user/diet'])
    return false;
};
