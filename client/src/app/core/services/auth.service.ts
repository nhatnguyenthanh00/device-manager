import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { HttpService } from './http.service';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private httpService: HttpService) {}
  tokenService: TokenService = new TokenService();

  login(username: string, password: string): Observable<string | null> {
    const payload = { username, password };
    return this.httpService.post<any>('/api/login', payload).pipe(
      map((response) => {
        console.log('In auth service');
        console.log(response);
        if(response?.errCode !== 0) return response?.errMsg;
        if(response?.data?.token){
          localStorage.setItem('currentToken', (response?.data?.token));
          return null;
        } 
        return 'System Busy, Please try again later';
      }),
      catchError(() => of('System Busy, Please try again later'))
    );
  }

  logout() {
    localStorage.removeItem('currentToken');
  }
}
