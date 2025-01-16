import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { TokenService } from '../../core/services/token.service';
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
    this.authService.login(this.username, this.password).subscribe((result) => {
      if (result == null) {
        // const token = result?.data?.token;

        let token = localStorage.getItem('currentToken');
        if (token == null) token = '';
        if (this.tokenService.getRoleFromToken(token) === 'ROLE_ADMIN') {
          // alert('Welcome admin');
          this.router.navigate(['/admin/homepage']);
        } else {
          if (this.tokenService.getRoleFromToken(token) === 'ROLE_USER') {
            // alert('Welcome user');
            this.router.navigate(['/dashboard']);
          }
          else {
            this.router.navigate(['/notfound']);
          }
        }
      } else {
        alert(result);
      }
    });
  }
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}
