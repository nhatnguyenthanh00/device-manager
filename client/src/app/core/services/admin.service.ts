import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AxiosResponse } from 'axios';
import { HttpService } from './http.service';
import { User } from '../../models/user.model';
import { HttpClient, HttpParams } from '@angular/common/http';
@Injectable({
  providedIn: 'root',
})
export class AdminService {
  constructor(private http: HttpClient, private httpService: HttpService) {}
  getListUser(params : object): Observable<User[]> {

    console.log('In admin service');
    console.log(params);

    return this.httpService.get<User[]>('/api/admin/user-list', params).pipe(
      catchError((error) => {
        console.error('Error fetching user list:', error);
        return of([] as User[]);
      })
    );
  }
}
