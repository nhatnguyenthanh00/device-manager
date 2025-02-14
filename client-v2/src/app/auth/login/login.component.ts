import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { TokenService } from '../../core/services/token.service';
import { Router } from '@angular/router';
import { STORAGE_KEYS } from '../../core/constants';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  constructor(private authService: AuthService, private tokenService: TokenService, private router: Router) {
  }

  username: string = '';
  password: string = '';
  showPassword: boolean = false;
  onSubmit() {

    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {
        const token = response?.token;
        if (token) {
          localStorage.setItem(STORAGE_KEYS.TOKEN, token);
          if (this.tokenService.getRoleFromToken(token) === 'ROLE_ADMIN') {
            this.router.navigate(['/admin/homepage']);
          } else {
            this.router.navigate(['/homepage']);
          }
        }
      },
      error: (err) => {
        alert(err.error?.errMsg);
      },
    });
  }
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}
