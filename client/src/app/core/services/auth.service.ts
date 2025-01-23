import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { HttpService } from './http.service';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private httpService: HttpService, private tokenService: TokenService) {}

  /**
   * Logs in a user with the given username and password.
   *
   * @param username - The username of the user.
   * @param password - The password of the user.
   * @returns An observable of a string or null. If the request is successful, the observable will emit null. If the request fails, the observable will emit the error message.
   */
  login(username: string, password: string): Observable<string | null> {
    const payload = { username, password };
    return this.httpService.post<any>('/api/login', payload).pipe(
      // If the request is successful, return the token and store it in local storage
      map((response) => {
        if (response?.errMsg) return response?.errMsg;
        if (response?.data?.token) {
          localStorage.setItem('currentToken', (response?.data?.token));
          return null;
        } 
        // If the request fails, return an error message
        return 'System Busy, Please try again later';
      }),
      // If the request fails, catch the error and return an error message
      catchError(() => of('System Busy, Please try again later'))
    );
  }


  /**
   * Logs out the current user by removing the token from local storage.
   *
   * This method is called when the user clicks the logout button in the header.
   */
  logout() {
    localStorage.removeItem('currentToken');
  }
}
