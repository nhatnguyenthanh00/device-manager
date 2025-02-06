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
  login(username: string, password: string): Observable<any> {
    const payload = { username, password };
    return this.httpService.post<any>('/api/login', payload);
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
