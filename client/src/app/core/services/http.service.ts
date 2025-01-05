import { Injectable } from '@angular/core';
import axios, { AxiosRequestConfig, AxiosResponse } from 'axios';
import { from, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment'; ;

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  constructor() {}

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
