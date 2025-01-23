import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../services/token.service';
import { ToastrService } from 'ngx-toastr';
/**
 * Checks if the user is authenticated and has the required role
 * to access a specific route.
 *
 * @param route The route being accessed
 * @param state The current router state
 * @returns true if the user is authenticated and has the required role, otherwise false
 */
export const authGuard: CanActivateFn = (route, state) => {
  const tokenService = inject (TokenService);
  const toastr = inject(ToastrService);
  const router = inject(Router); 
  let currentToken = localStorage.getItem('currentToken');
  if(currentToken == null) currentToken = '';
  const role = tokenService.getRoleFromToken(currentToken);

  // If the user is not authenticated, redirect them to the login page
  if (!currentToken) {
    router.navigate(['/login']);
    return false;
  }

  // If the user is authenticated, check if the token is still valid
  if (currentToken && !tokenService.isTokenValid(currentToken)) {
    localStorage.removeItem('currentToken');
    toastr.error('Token expired, please login again.');
    router.navigate(['/login']);
    // Show a toast message indicating that the token has expired
    return false;
  }
  
  // Check if the user has the required role
  const requiredRole = route.data?.['role']; 

  if (requiredRole) {
    // If the user does not have the required role, redirect them to the unauthorized page
    if ( (role==null) || (role !== requiredRole)) {
      router.navigate(['/unauthorized']); 
      return false;
    }
  }

  return true;
};
