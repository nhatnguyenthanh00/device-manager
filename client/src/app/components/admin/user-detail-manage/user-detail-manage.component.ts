import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HomePageService } from '../../../core/services/home-page.service';
import { AdminService } from '../../../core/services/admin.service';
import { Subscription } from 'rxjs';
import { DeviceManageComponent } from '../device-manage/device-manage.component';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-detail-manage',
  standalone: true,
  imports: [DeviceManageComponent, CommonModule, FormsModule],
  templateUrl: './user-detail-manage.component.html',
  styleUrl: './user-detail-manage.component.css',
})
export class UserDetailManageComponent {
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
  private subscription: Subscription;
  constructor(
    private homePageService: HomePageService,
    private adminService: AdminService,
    private toastr: ToastrService
  ) {
    this.subscription = new Subscription();
  }
  ngOnInit() {
    this.subscription = this.homePageService.detailUserId$.subscribe(
      (userId) => {
        this.userId = userId;
        this.getUserDetails();
      }
    );
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  getUserDetails(page: number = 1) {
    if (this.userId == null) return;
    this.adminService.getUser(this.userId, page).subscribe((res) => {
      if (res.errMsg) {
        alert(res?.errMsg);
      } else {
        this.name = res?.data?.userDetail?.name;
        this.username = res?.data?.userDetail?.username;
        this.gender = res?.data?.userDetail?.gender;
        this.totalDevice = res?.data?.devices?.content?.length;
        this.listDevice = res?.data?.devices;
        this.role = res?.data?.userDetail?.role;
      }
    });
  }

  onDeviceUpdated(event: { page: number }) {
    console.log('hay hay update');
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
      this.adminService.updateUser(payload).subscribe((response) => {
        if (response?.errMsg) {
          this.toastr.error(response?.errMsg, 'Error');
        } else {
          this.toastr.success('User updated successfully!', 'Success');
          this.getUserDetails();
        }
      });
    }
  }

  onResetPassword(event: Event) {
    event.preventDefault();
    const payload = {
      userId: this.userId,
      password: this.newPassword,
      adminPassword: this.adminPassword,
    };
    this.adminService.resetPassword(payload).subscribe({
      next: (response) => {
        console.log('In onResetPassword');
        console.log(response);
        if(response && response.errMsg == null && response.data == true){
          this.toastr.success('User password reset successfully!', 'Success');
          this.getUserDetails();
        }
        else{
          this.toastr.error(response?.errMsg || 'Internal server error', 'Error');
        }
      }
    });
  }
}
