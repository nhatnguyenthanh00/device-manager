import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { TokenService } from '../../../core/services/token.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  showPassword: boolean = false;
  authService = inject(AuthService);
  tokenService = inject(TokenService);
  router = inject(Router);
  onSubmit() {

    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {
        const token = response?.token;
        if (token) {
          localStorage.setItem('currentToken', token);
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
