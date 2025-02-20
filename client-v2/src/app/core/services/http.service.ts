import { Injectable } from '@angular/core';
import { Observable, throwError, timer, MonoTypeOperatorFunction } from 'rxjs';
import { retry, timeout, catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class HttpService {
  private readonly TIMEOUT = 30000; // 30 seconds timeout
  private readonly MAX_RETRIES = 10;
  private readonly RETRY_DELAY = 100; // 1 second delay

  constructor(private http: HttpClient) {}

  private retryStrategy<T>(): MonoTypeOperatorFunction<T> {
    return retry({
      count: this.MAX_RETRIES,
      delay: (error, retryCount) => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return throwError(() => error);
        }
        console.log(`Retry lần ${retryCount} sau ${this.RETRY_DELAY}ms`);
        return timer(this.RETRY_DELAY);
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