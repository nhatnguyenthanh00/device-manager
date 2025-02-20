import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../../../core/services/token.service';
import { ROLES, STORAGE_KEYS } from '../../../core/constants';
@Component({
  selector: 'app-check-login',
  templateUrl: './check-login.component.html',
  styleUrl: './check-login.component.css'
})
export class CheckLoginComponent implements OnInit {
  constructor(private router: Router, private tokenService: TokenService) {}

  ngOnInit(): void {
    const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
    
    // Nếu chưa đăng nhập, chuyển hướng tới /login
    if (!token) {
      this.router.navigate(['/login']);
    } else {
      // Nếu đã đăng nhập, kiểm tra vai trò người dùng
      const role = this.tokenService.getRoleFromToken(token);

      if (role === ROLES.ROLE_USER) {
        this.router.navigate(['/homepage']);  // Dành cho người dùng
      } else if (role === ROLES.ROLE_ADMIN) {
        this.router.navigate(['/admin/homepage']);  // Dành cho admin
      } else {
        this.router.navigate(['/login']);  // Trường hợp không xác định vai trò
      }
    }
  }
}
