import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs';
/**
 * HTTP Interceptor để thêm Authorization header với token.
 * Nếu token không hợp lệ, sẽ xóa token và chuyển hướng đến trang login.
 * @param req - Request gốc
 * @param next - Next function để tiếp tục chuỗi request
 * @returns Observable chứa response
 */
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const currentToken = localStorage.getItem('currentToken');

  if (currentToken != null && currentToken !== '') {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${currentToken}`,
      },
    });

    return next(clonedRequest).pipe(
      catchError((error) => {
        if (error.status === 401 || error.status === 403) {
          localStorage.removeItem('currentToken');
          router.navigate(['/login']);
        }
        throw error;
      })
    );
  }

  return next(req); 
};
