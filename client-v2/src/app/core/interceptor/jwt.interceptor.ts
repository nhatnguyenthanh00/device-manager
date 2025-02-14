import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, Observable } from 'rxjs';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { AuthService } from '../../auth/services/auth.service';
/**
 * HTTP Interceptor để thêm Authorization header với token.
 * Nếu token không hợp lệ, sẽ xóa token và chuyển hướng đến trang login.
 * @param req - Request gốc
 * @param next - Next function để tiếp tục chuỗi request
 * @returns Observable chứa response
 */
@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>{
    const token = localStorage.getItem('currentToken');
    if (token!=null && token != '') {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }
    return next.handle(req).pipe(
      catchError((error) => {
        if (error.status === 401 || error.status === 403) {
          localStorage.removeItem('currentToken');
          this.router.navigate(['/login']);
        }
        throw error;
      })
    );
  }
}
