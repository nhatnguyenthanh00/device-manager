import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../../../core/services/token.service';
@Component({
  selector: 'app-check-login',
  templateUrl: './check-login.component.html',
  styleUrl: './check-login.component.css'
})
export class CheckLoginComponent implements OnInit {
  constructor(private router: Router, private tokenService: TokenService) {}

  ngOnInit(): void {
    const token = localStorage.getItem('currentToken');
    
    // Nếu chưa đăng nhập, chuyển hướng tới /login
    if (!token) {
      this.router.navigate(['/login']);
    } else {
      // Nếu đã đăng nhập, kiểm tra vai trò người dùng
      const role = this.tokenService.getRoleFromToken(token);

      if (role === 'ROLE_USER') {
        this.router.navigate(['/homepage']);  // Dành cho người dùng
      } else if (role === 'ROLE_ADMIN') {
        this.router.navigate(['/admin/homepage']);  // Dành cho admin
      } else {
        this.router.navigate(['/login']);  // Trường hợp không xác định vai trò
      }
    }
  }
}
