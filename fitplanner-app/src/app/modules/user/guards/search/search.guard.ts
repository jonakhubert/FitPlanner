import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const searchGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const dateParam = route.queryParamMap.get('date');
  const typeParam = route.queryParamMap.get('type');
  const dateFormatRegex = /^\d{4}-\d{2}-\d{2}$/;

  const validTypes = ['CARDIO', 'STRENGTH'];
  const isValidType = validTypes.includes(typeParam!);

  if(dateParam && isValidType && dateParam.match(dateFormatRegex))
    return true;
  else {
    const currentUrl = state.url;

    if(currentUrl.includes('/user/workout'))
      router.navigate(['user/workout']);
    else if(currentUrl.includes('/user/diet'))
      router.navigate(['user/diet']);

    return false;
  }
};
