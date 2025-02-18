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
    this.errChangePasswordMsg = {};
    for (const key in this.changePasswordForm.controls) {
      const control = this.changePasswordForm.get(key);
      if (control?.invalid) {
        if (control.errors?.['required']) {
          this.errChangePasswordMsg[key] = `${key.charAt(0).toUpperCase() + key.slice(1)} is required.`;
        } else if (control.errors?.['minlength']) {
          this.errChangePasswordMsg[key] = `${key.charAt(0).toUpperCase() + key.slice(1)} must be at least ${
            control.errors['minlength'].requiredLength
          } characters.`;
        } else if (control.errors?.['pattern']) {
          this.errChangePasswordMsg[key] = this.getPatternErrorMessage(key);
        }
      }
    }
    if(this.changePasswordForm.value.confirmPassword){
      if(this.changePasswordForm.value.newPassword !== this.changePasswordForm.value.confirmPassword){
        this.errChangePasswordMsg['confirmPassword'] = 'Passwords do not match!';
      }
    }
    return Object.keys(this.errChangePasswordMsg).length === 0;
  }

  getPatternErrorMessage(key: string): string {
    switch (key) {
      case 'newPassword':
        return 'Password must be contain at least one uppercase letter, one lowercase letter, one number, and one special character.';
      default:
        return 'Invalid input.';
    }
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
