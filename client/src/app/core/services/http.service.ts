import { Injectable } from '@angular/core';
import axios, { AxiosRequestConfig, AxiosResponse } from 'axios';
import { from, Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class HttpService {
  constructor(private http: HttpClient) {}

  get<T>(url: string, params: { [key: string]: any }): Observable<T> {
    const httpParams = new HttpParams({
      fromObject: params, // Chuyển object thành query parameters
    });
    return this.http.get<T>(`${environment.apiBaseUrl}${url}`, {
      params: httpParams,
    });
  }

  post<T>(url: string, body: any): Observable<any> {
    return this.http.post<T>(`${environment.apiBaseUrl}${url}`, body);
  }

  put<T>(url: string, body: any): Observable<any> {
    return this.http.put<T>(`${environment.apiBaseUrl}${url}`, body);
  }

  delete<T>(url: string, body: any): Observable<any> {
    return this.http.delete<T>(`${environment.apiBaseUrl}${url}`, {
      body: body // This is the key change - wrap the body in a 'body' property
    });
  }
}
