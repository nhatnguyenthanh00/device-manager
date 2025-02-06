import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../core/services/user.service';
import { ToastrService } from 'ngx-toastr';
@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css',
})
export class UserProfileComponent {
  userProfile: any = null;
  oldPassword: string = '';
  newPassword: string = '';
  showPassword: boolean = false;
  confirmPassword: string = '';
  errorMsg: string = '';

  constructor(private userService: UserService, private toastr: ToastrService) {}
  ngOnInit() {
    // Load user profile data
    this.loadUserProfile();
  }

  loadUserProfile() {
    // Call your service to get user profile

    this.userService.getUserProfile().subscribe({
      next: (response: any) => {
        this.userProfile = response;
      },
      error: (err) => {
        alert(err.error?.errMsg);
      },
    });
  }

  onSubmit() {}
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  changePassword() {
    // Reset error message
    this.errorMsg = '';

    // Validation
    if (!this.oldPassword || !this.newPassword || !this.confirmPassword) {
      this.errorMsg = 'All fields are required';
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      this.errorMsg = 'New passwords do not match';
      return;
    }

    if (this.newPassword.length < 8) {
      this.errorMsg = 'New password must be at least 8 characters long';
      return;
    }

    // Password strength validation
    ''
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,}$/;
    if (!passwordRegex.test(this.newPassword)) {
      this.errorMsg = 'Password must be contain at least one uppercase letter, one lowercase letter, one number, and one special character';
      return;
    } 

    this.userService.changePassword(this.oldPassword, this.newPassword).subscribe({
      next: (response) => {
        this.toastr.success('Password changed successfully!', 'Success');
        this.oldPassword = '';
        this.newPassword = '';
        this.confirmPassword = '';
        this.showPassword = false;
      },
      error: (err) => {
        this.toastr.error(err.error?.errMsg, 'Error');
      },
    })
  }
}
