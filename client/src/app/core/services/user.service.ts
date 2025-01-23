import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private httpService: HttpService) {}

  getUserProfile(): Observable<any> {
    const params = {};

    return this.httpService.get<any>('/api/profile', params).pipe(
      catchError(error => of(null))
    );
  }

  changePassword(oldPassword: string, newPassword: string): Observable<any> {
    return this.httpService.post<any>('/api/change-password', {
      oldPassword,
      newPassword,
    });
  }
}
