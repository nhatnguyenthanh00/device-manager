import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpService } from '../../core/services/http.service';
import {STORAGE_KEYS} from '../../core/constants';
import { API_ENDPOINT } from '../../core/constants';
@Injectable()
export class AuthService {
  constructor(private httpService: HttpService) {}

  /**
   * Logs in a user with the given username and password.
   *
   * @param username - The username of the user.
   * @param password - The password of the user.
   * @returns An observable of a string or null. If the request is successful, the observable will emit null. If the request fails, the observable will emit the error message.
   */
  login(username: string, password: string): Observable<any> {
    const payload = { username, password };
    return this.httpService.post<any>(API_ENDPOINT.LOGIN, payload);
  }


  /**
   * Logs out the current user by removing the token from local storage.
   *
   * This method is called when the user clicks the logout button in the header.
   */
  logout() {
    localStorage.removeItem(STORAGE_KEYS.TOKEN);
  }
}
