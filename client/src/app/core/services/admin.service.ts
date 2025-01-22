import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AxiosResponse } from 'axios';
import { HttpService } from './http.service';
import { User } from '../../models/user.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { UserList } from '../../models/user-list.model';
import { NewUser } from '../../models/new-user.model';
@Injectable({
  providedIn: 'root',
})
export class AdminService {
  constructor(private httpService: HttpService) {}
  getListUser(params : object): Observable<any> {

    console.log('In admin service');
    console.log(params);

    return this.httpService.get<UserList>('/api/admin/user', params).pipe(
      catchError((error) => {
        console.error('Error fetching user list:', error);
        return of(null);
      })
    );
  }

  getUser(userId: string, page: number): Observable<any> {
    return this.httpService.post<any>('/api/admin/user-detail', { userId: userId, page:page });
  }

  addUser(user: NewUser): Observable<any> {
    return this.httpService.post<any>('/api/admin/user', user);
  }

  updateUser(payload : any):Observable<any> {
    return this.httpService.put<any>('/api/admin/user',payload);
  }

  resetPassword(payload : any):Observable<any> {
    return this.httpService.put<any>('/api/admin/reset-password',payload);
  }

  deleteUser(userId: string): Observable<any> {
    return this.httpService.delete<any>('/api/admin/user',{id:userId});
  }

  getUsernames(): Observable<any> {
    return this.httpService.get<any>('/api/admin/user-name',{});
  }
}
