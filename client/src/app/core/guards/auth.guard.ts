import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../services/token.service';

export const authGuard: CanActivateFn = (route, state) => {
  const tokenService = inject (TokenService);
  const router = inject(Router); 
  const currentUser = localStorage.getItem('currentUser');

  if (!currentUser) {
    router.navigate(['/login']);
    return false;
  }

  const user = JSON.parse(currentUser);
  const requiredRole = route.data?.['role']; 

  if (user.token && !tokenService.isTokenValid(user.token)) {
    localStorage.removeItem('currentUser');
    router.navigate(['/login']);
    return false;
  }
  
  if (requiredRole) {
    if (!user.role || user.role !== requiredRole) {
      router.navigate(['/unauthorized']); 
      return false;
    }
  }

  return true;
};
