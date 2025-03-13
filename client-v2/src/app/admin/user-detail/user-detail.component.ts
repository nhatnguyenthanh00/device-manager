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
  showPassword: boolean = false;
  showAdminPassword: boolean = false;
  totalDevice: number = 0;
  userInfoForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[A-Za-zÀ-ỹ\s]+$/)
    ]),
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
      Validators.pattern(/^[A-Za-z0-9]+$/)
    ]),
    gender: new FormControl('', [Validators.required]),
    role: new FormControl('', [Validators.required]),
  })
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

        this.userInfoForm.patchValue({
          name: res?.userDetail?.name,
          username: res?.userDetail?.username,
          gender: res?.userDetail?.gender,
          role: res?.userDetail?.role
        });

        this.totalDevice = res?.devices?.content?.length;
        this.listDevice = res?.devices;
      },
      error: (err) => {
        this.toastr.error(err.error?.errMsg, 'Error');
      }
    });
  }

  onDeviceUpdated(event: { page: number }) {
    this.getUserDetails(event.page);
  }

  onUpdateUser(event: Event) {
    event.preventDefault();
    if (this.userInfoForm.valid) {
      const payload = {
        userId: this.userId,
        name: this.userInfoForm.get('name')?.value,
        username: this.userInfoForm.get('username')?.value,
        gender: this.userInfoForm.get('gender')?.value,
        role: this.userInfoForm.get('role')?.value,
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
