import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const exerciseSearchGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const dateParam = route.queryParamMap.get('date');
  const typeParam = route.queryParamMap.get('type');
  const dateFormatRegex = /^\d{4}-\d{2}-\d{2}$/;

  const validTypes = ['CARDIO', 'STRENGTH'];
  const isValidType = validTypes.includes(typeParam!);

  if(dateParam && isValidType && dateParam.match(dateFormatRegex))
    return true;
  else {
    router.navigate(['user/workout']);

    return false;
  }
};
