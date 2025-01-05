import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { HttpService } from './http.service';
import { AxiosResponse } from 'axios';
import { TokenService } from './token.service';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private httpService: HttpService) {}
  tokenService: TokenService = new TokenService();

  login(username: string, password: string): Observable<boolean> {
    const payload = { username, password };
    return this.httpService.makeHttpRequest<any>('/api/login', 'post', payload).pipe(
      map((response: AxiosResponse<any>) => {
        if (response && response.data.token) {
          const token = response.data.token;
          const decodedToken = this.tokenService.decodeToken(token);
          const currentUser = {
            username: decodedToken.sub, // username
            role: decodedToken.role,   // role từ JWT
            token: token,
          };
          localStorage.setItem('currentUser', JSON.stringify(currentUser));
          return true;
        }
        return false;
      }),
      catchError(() => of(false))
    );
  }

  logout() {
    localStorage.removeItem('currentUser');
  }
}
