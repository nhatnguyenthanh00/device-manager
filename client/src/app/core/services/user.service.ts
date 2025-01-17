import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';
  constructor(private httpService: HttpService) {}

  async getUsers() {
    try{
      const response = await fetch(this.apiUrl);
      return response.json();
    } catch (error) {
      console.error(error);
      return [];
    }
  }
  getUserProfile():Observable<any>{
    const params = {
    }
    return this.httpService.get<any>('/api/profile',params).pipe(
      catchError((error) => {
        console.error('Error fetching user profile:', error);
        return of(null);
      })
    );
  }

  changePassword(oldPassword:string,newPassword:string):Observable<any>{
    return this.httpService.post<any>('/api/changePassword',{
      oldPassword,
      newPassword
    });
  }
}
