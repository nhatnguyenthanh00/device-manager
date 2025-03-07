import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
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
  showPassword: boolean = false;
  showAdminPassword: boolean = false;
  newPassword: string = '';
  adminPassword: string = '';
  totalDevice: number = 0;
  role: string = '';
  resetPasswordForm = new FormGroup({
      newPassword: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/)
      ]),
      adminPassword: new FormControl('', [
        Validators.required
      ]),
  });
  constructor(private homePageService: HomePageService, private adminService: AdminService, private toastr: ToastrService, private route: ActivatedRoute, private router : Router) {}
  ngOnInit() {
    this.userId = this.route.snapshot.paramMap.get('userId');
    this.getUserDetails();
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
  toggleAdminPasswordVisibility() {
    this.showAdminPassword = !this.showAdminPassword;
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

  onResetPassword() {
    if(this.resetPasswordForm.valid){
      const payload = {
        userId: this.userId,
        password: this.resetPasswordForm.get('newPassword')?.value,
        adminPassword: this.resetPasswordForm.get('adminPassword')?.value,
      };
      this.adminService.resetPassword(payload).subscribe({
        next: (response) => {
          this.toastr.success('User password reset successfully!', 'Success');
          this.resetPasswordForm.reset();
          this.resetPasswordForm.controls['newPassword'].setErrors(null);
          this.resetPasswordForm.controls['adminPassword'].setErrors(null);
        },
        error: (err) => {
          this.toastr.error(err.error?.errMsg, 'Error');
        },
      });
    }
  }

  backHomePage(){
    this.homePageService.setActiveTab('user');
    this.router.navigate(['/'+ROUTES.ADMIN_HOME]);
  }
}
