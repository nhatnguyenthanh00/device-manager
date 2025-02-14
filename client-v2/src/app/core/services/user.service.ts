import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { Observable, of } from 'rxjs';
import { API_ENDPOINT } from '../constants';
@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private httpService: HttpService) {}

  getUserProfile(): Observable<any> {
    return this.httpService.get<any>(API_ENDPOINT.PROFILE);
  }

  changePassword(oldPassword: string, newPassword: string): Observable<any> {
    return this.httpService.post<any>(API_ENDPOINT.CHANGE_PASSWORD, {
      oldPassword,
      newPassword,
    });
  }
}
