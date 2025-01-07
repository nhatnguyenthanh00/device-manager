import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
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
  router = inject(Router); 
  onSubmit() {
    console.log('Username: ' + this.username);
    this.authService.login(this.username, this.password).subscribe((result) => {
      if (result) {
        console.log('Login successful');
        const storedUser = localStorage.getItem('currentUser');
        let currentUser = null;
        if (storedUser !== null) {
          currentUser = JSON.parse(storedUser);
        }
        if (currentUser !== null) {
          if(currentUser.role === 'ROLE_ADMIN') {
            // alert('Welcome admin');
            this.router.navigate(['/admin/homepage']);
          }
          else{
            // alert('Welcome user');
            this.router.navigate(['/dashboard']);
          }
        }

      } else {
        alert('Login failed');
      }
    });
  }
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}
