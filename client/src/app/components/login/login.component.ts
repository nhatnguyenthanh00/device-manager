import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  showPassword: boolean = false;
  authService = inject(AuthService);
  onSubmit() {
     console.log('Username: ' + this.username);
     this.authService.login(this.username, this.password).subscribe((result) => {
       if (result) {
         console.log('Login successful');
       } else {
         console.log('Login failed');
       }
     });

  }
  togglePasswordVisibility() { this.showPassword = !this.showPassword; }
}
