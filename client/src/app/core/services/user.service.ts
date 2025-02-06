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
    return this.httpService.get<any>('/api/profile');
  }

  changePassword(oldPassword: string, newPassword: string): Observable<any> {
    return this.httpService.post<any>('/api/change-password', {
      oldPassword,
      newPassword,
    });
  }
}
