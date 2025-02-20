import { Injectable } from '@angular/core';
import { Observable, throwError, timer, MonoTypeOperatorFunction } from 'rxjs';
import { retry, timeout, catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { NUMBERS } from '../constants';
@Injectable({
  providedIn: 'root',
})
export class HttpService {
  private readonly TIMEOUT = NUMBERS.NUMBER_10000; // 10 seconds timeout
  private readonly MAX_RETRIES = NUMBERS.NUMBER_15;
  private readonly RETRY_DELAY = NUMBERS.NUMBER_100; // 0.1 second delay

  constructor(private http: HttpClient) {}

  private retryStrategy<T>(): MonoTypeOperatorFunction<T> {
    return retry({
      count: this.MAX_RETRIES,
      delay: (error, retryCount) => {
        if (error instanceof HttpErrorResponse) {
          // Không retry nếu lỗi là 400 hoặc 401
          if (error.status === 400 || error.status === 401) {
            return throwError(() => error);
          }
  
          // Retry nếu là lỗi 5xx hoặc lỗi mạng (status = 0)
          if (error.status >= 500 || error.status === 0) {
            console.log(`Retry lần ${retryCount} sau ${this.RETRY_DELAY}ms`);
            return timer(this.RETRY_DELAY);
          }
        }
  
        return throwError(() => error);
      }
    });
  }

  get<T>(url: string, params?: { [key: string]: any }): Observable<T> {
    const httpParams = new HttpParams({
      fromObject: params,
    });
    
    return this.http.get<T>(`${environment.apiBaseUrl}${url}`, {
      params: httpParams,
    }).pipe(
      timeout(this.TIMEOUT),
      this.retryStrategy<T>(),
      catchError(error => this.handleError(error))
    );
  }

  post<T>(url: string, body: any, params?: { [key: string]: any }): Observable<T> {
    const httpParams = new HttpParams({
      fromObject: params,
    });
    
    return this.http.post<T>(`${environment.apiBaseUrl}${url}`, body, {
      params: httpParams
    }).pipe(
      timeout(this.TIMEOUT),
      this.retryStrategy<T>(),
      catchError(error => this.handleError(error))
    );
  }

  put<T>(url: string, body: any): Observable<T> {
    return this.http.put<T>(`${environment.apiBaseUrl}${url}`, body).pipe(
      timeout(this.TIMEOUT),
      this.retryStrategy<T>(),
      catchError(error => this.handleError(error))
    );
  }

  delete<T>(url: string, params?: any): Observable<T> {
    return this.http.delete<T>(`${environment.apiBaseUrl}${url}`, {
      params: params
    }).pipe(
      timeout(this.TIMEOUT),
      this.retryStrategy<T>(),
      catchError(error => this.handleError(error))
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An error occurred';

    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => error);
  }

}