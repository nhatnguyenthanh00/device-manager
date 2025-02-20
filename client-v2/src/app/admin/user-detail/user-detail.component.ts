import { Component, inject } from '@angular/core';
import { AdminService } from '../services/admin.service';
import { HomePageService } from '../../core/services/home-page.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { ROUTES } from '../../core/constants';
@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrl: './user-detail.component.css'
})
export class UserDetailComponent {
  listDevice: any;
  userId: string | null = null;
  name: string = '';
  username: string = '';
  gender: string = '';
  password: string = '';
  showPassword: boolean = false;
  newPassword: string = '';
  adminPassword: string = '';
  totalDevice: number = 0;
  role: string = '';
  constructor(private homePageService: HomePageService, private adminService: AdminService, private toastr: ToastrService, private route: ActivatedRoute, private router : Router) {}
  ngOnInit() {
    this.userId = this.route.snapshot.paramMap.get('userId');
    this.getUserDetails();
  }

  getUserDetails(page: number = 1) {
    if (this.userId == null) return;

    this.adminService.getUser(this.userId, page).subscribe({
      next: (res) => {
        this.name = res?.userDetail?.name;
        this.username = res?.userDetail?.username;
        this.gender = res?.userDetail?.gender;
        this.totalDevice = res?.devices?.content?.length;
        this.listDevice = res?.devices;
        this.role = res?.userDetail?.role;
      },
      error: (err) => {
        this.toastr.error(err.error?.errMsg, 'Error');
      }
    });
  }

  onDeviceUpdated(event: { page: number }) {
    this.getUserDetails(event.page);
  }

  generatePassword() {
    const upperCaseChars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    const lowerCaseChars = 'abcdefghijklmnopqrstuvwxyz';
    const digits = '0123456789';
    const specialChars = '!@#$%^&*()';
    const allChars = upperCaseChars + lowerCaseChars + digits + specialChars;

    // Ensure at least one character from each required category
    const password = [
      upperCaseChars[Math.floor(Math.random() * upperCaseChars.length)], // One uppercase
      lowerCaseChars[Math.floor(Math.random() * lowerCaseChars.length)], // One lowercase
      digits[Math.floor(Math.random() * digits.length)], // One digit
      specialChars[Math.floor(Math.random() * specialChars.length)], // One special char
    ];

    // Fill the remaining characters with random choices from all available characters
    while (password.length < 8) {
      password.push(allChars[Math.floor(Math.random() * allChars.length)]);
    }

    // Shuffle the password to randomize the order
    this.newPassword = password.sort(() => Math.random() - 0.5).join('');
  }

  validateInput(): boolean {
    if (!this.name) {
      this.toastr.error('Name is required', 'Error');
      return false;
    } else {
      const trimmedName = this.name.trim();
      const nameRegex = /^[\p{L}\s]+$/u;
      if (!nameRegex.test(trimmedName)) {
        this.toastr.error('Name can only contain letters and spaces');
        return false;
      }
      this.name = trimmedName;
    }
    if (!this.username) {
      this.toastr.error('Username is required');
      return false;
    } else {
      if (this.username.length < 6) {
        this.toastr.error('Username must be at least 6 characters');
        return false;
      }
      if (this.username.includes(' ')) {
        this.toastr.error('Username cannot contain spaces');
        return false;
      }
    }

    return true;
  }

  onUpdateUser(event: Event) {
    event.preventDefault();
    if (this.validateInput()) {
      const payload = {
        userId: this.userId,
        name: this.name,
        username: this.username,
        gender: this.gender,
        role: this.role,
      };

      this.adminService.updateUser(payload).subscribe({
        next: (response) => {
          this.toastr.success('User updated successfully!', 'Success');
          this.getUserDetails();
        },
        error: (err) => {
          this.toastr.error(err.error?.errMsg, 'Error');
        }
      });
    }
  }

  onResetPassword(event: Event) {
    event.preventDefault();
    if (!this.newPassword || !this.adminPassword) {
      this.toastr.error('All fields are required', 'Error');
      return;
    }

    if (this.newPassword.length < 8) {
      this.toastr.error('New password must be at least 8 characters long', 'Error');
      return;
    }

    const passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,}$/;
    if (!passwordRegex.test(this.newPassword)) {
      this.toastr.error(
        'Password must be contain at least one uppercase letter, one lowercase letter, one number, and one special character',
        'Error'
      );
      return;
    }
    const payload = {
      userId: this.userId,
      password: this.newPassword,
      adminPassword: this.adminPassword,
    };
    this.adminService.resetPassword(payload).subscribe({
      next: (response) => {
        this.toastr.success('User password reset successfully!', 'Success');
        this.getUserDetails();
      },
      error: (err) => {
        this.toastr.error(err.error?.errMsg, 'Error');
      },
    });
  }

  backHomePage(){
    this.homePageService.setActiveTab('user');
    this.router.navigate(['/'+ROUTES.ADMIN_HOME]);
  }
}
