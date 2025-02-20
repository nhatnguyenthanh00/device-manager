import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { UserService } from '../../../core/services/user.service';
import { ToastrService } from 'ngx-toastr';
@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit{
  userProfile: any = null;
  showPassword: boolean = false;
  changePasswordForm = new FormGroup({
    oldPassword: new FormControl('', [Validators.required]),
    newPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/)
    ]),
    confirmPassword: new FormControl('', [Validators.required]),
  })
  errChangePasswordMsg: { [key: string]: string } = {};

  constructor(private userService: UserService, private toastr: ToastrService) {}
  ngOnInit() {
    // Load user profile data
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

  validateChangePassword(): boolean{
    if (this.changePasswordForm.value.confirmPassword) {
      if (this.changePasswordForm.value.newPassword !== this.changePasswordForm.value.confirmPassword) {
        this.changePasswordForm.controls['confirmPassword'].setErrors({ mismatch: true });
        return false;
      }
    }
    return this.changePasswordForm.valid;
  }

  changePassword() {
    if(this.validateChangePassword()){
      this.userService.changePassword(this.changePasswordForm.value.oldPassword as string, this.changePasswordForm.value.newPassword as string).subscribe({
        next: (response) => {
          this.toastr.success('Password changed successfully!', 'Success');
          this.changePasswordForm.reset();
          this.showPassword = false;
        },
        error: (err) => {
          this.toastr.error(err.error?.errMsg, 'Error');
        },
      });
    }
  }
}
