import { Injectable } from '@angular/core';
import axios, { AxiosRequestConfig, AxiosResponse } from 'axios';
import { from, Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  constructor(private http: HttpClient) {}

  get<T>(url: string, params: { [key: string]: any }): Observable<T> {
    const httpParams = new HttpParams({
      fromObject: params, // Chuyển object thành query parameters
    });
    return this.http.get<T>(`${environment.apiBaseUrl}${url}`, {params: httpParams});
  }

  post<T>(url: string, body: any): Observable<any> {
    return this.http.post<T>(`${environment.apiBaseUrl}${url}`, body);
  }

  put<T>(url: string, body: any): Observable<any> {
    return this.http.put<T>(`${environment.apiBaseUrl}${url}`, body);
  }

  delete<T>(url: string): Observable<any> {
    return this.http.delete<T>(`${environment.apiBaseUrl}${url}`);
  }

  // Reusable HTTP request function
  makeHttpRequest<T>(url: string, method: string, payload: any = null): Observable<AxiosResponse<T>> {
    let apiUrl = environment.apiBaseUrl + url;

    const config: AxiosRequestConfig = {
      url: apiUrl,
      method,
      data: payload,
      headers: {
        'Content-Type': 'application/json'
      }
    };

    return from(axios.request<T>(config)).pipe(
      catchError(error => {
        console.error('HTTP request error: ', error);
        throw error; // Re-throw error
      })
    );
  }
}
