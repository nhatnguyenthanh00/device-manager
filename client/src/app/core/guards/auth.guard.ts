import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../services/token.service';

export const authGuard: CanActivateFn = (route, state) => {
  const tokenService = inject (TokenService);
  const router = inject(Router); 
  let currentToken = localStorage.getItem('currentToken');
  if(currentToken == null) currentToken = '';
  const role = tokenService.getRoleFromToken(currentToken);

  if (!currentToken) {
    router.navigate(['/login']);
    return false;
  }

  const requiredRole = route.data?.['role']; 

  if (currentToken && !tokenService.isTokenValid(currentToken)) {
    localStorage.removeItem('currentToken');
    router.navigate(['/login']);
    alert('Token expired, please login again.');
    return false;
  }
  
  if (requiredRole) {
    if ( (role==null) || (role !== requiredRole)) {
      router.navigate(['/unauthorized']); 
      return false;
    }
  }

  return true;
};
