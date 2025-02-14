import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class HttpService {
  constructor(private http: HttpClient) {}

  get<T>(url: string, params?: { [key: string]: any }): Observable<T> {
    const httpParams = new HttpParams({
      fromObject: params,
    });
    return this.http.get<T>(`${environment.apiBaseUrl}${url}`, {
      params: httpParams,
    });
  }

  post<T>(url: string, body: any, params?: { [key: string]: any }): Observable<any> {
    const httpParams = new HttpParams({
      fromObject: params,
    });
    return this.http.post<T>(`${environment.apiBaseUrl}${url}`, body, {
      params: httpParams
    });
  }

  put<T>(url: string, body: any): Observable<any> {
    return this.http.put<T>(`${environment.apiBaseUrl}${url}`, body);
  }

  delete<T>(url: string, params?: any): Observable<any> {
    return this.http.delete<T>(`${environment.apiBaseUrl}${url}`, {
      params: params
    });
  }
}
