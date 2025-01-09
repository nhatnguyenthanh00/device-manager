import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AxiosResponse } from 'axios';
import { HttpService } from './http.service';
import { User } from '../../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private httpService: HttpService) {}
  getListUser(): Observable<any[]> {
    return this.httpService.makeHttpRequest<any>('/api/admin/user', 'get').pipe(
      map((response: AxiosResponse<any>) => {
        console.log('In admin service '+ response);
        return response.data;
      }),
      catchError(() => of([]))
    );
  }
}
