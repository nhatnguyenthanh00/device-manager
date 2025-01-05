import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router); // Sử dụng inject API để lấy Router
  const currentUser = localStorage.getItem('currentUser');

  // Kiểm tra xem user có đăng nhập không
  if (currentUser) {
    const token = JSON.parse(currentUser).token;

    if (token) {
      // Thêm Authorization header với token
      const clonedRequest = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });

      return next(clonedRequest).pipe(
        catchError((error) => {
          // Xử lý lỗi 401/403
          if (error.status === 401 || error.status === 403) {
            localStorage.removeItem('currentUser'); // Xóa thông tin người dùng
            router.navigate(['/login']); // Chuyển hướng đến trang login
          }
          throw error; // Để các interceptor hoặc subscriber khác xử lý lỗi
        })
      );
    }
  }

  return next(req); // Nếu không có token, tiếp tục request gốc
};
