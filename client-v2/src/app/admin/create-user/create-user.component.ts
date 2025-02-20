import { Component, EventEmitter, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../services/admin.service';
import { ToastrService } from 'ngx-toastr';
@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrl: './create-user.component.css',
})
export class CreateUserComponent {
  @Output() userCreated = new EventEmitter<void>();
  @Output() closeModal = new EventEmitter<void>();
  constructor(private adminService: AdminService, private toastr: ToastrService) {}
  
  userForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[\p{L}\s]+$/u), // Chỉ cho phép chữ cái và khoảng trắng
    ]),
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
      Validators.pattern(/^\S+$/), // Không chứa khoảng trắng
    ]),
    gender: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required), // Chỉ đọc
  });

  errMsg: { [key: string]: string } = {};

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
    this.userForm.get('password')?.setValue(password.sort(() => Math.random() - 0.5).join(''));
  }

  validateForm(): boolean {
    this.errMsg = {}; // Reset thông báo lỗi
    for (const key in this.userForm.controls) {
      const control = this.userForm.get(key);
      if (control?.invalid) {
        if (control.errors?.['required']) {
          this.errMsg[key] = `${key.charAt(0).toUpperCase() + key.slice(1)} is required.`;
        } else if (control.errors?.['minlength']) {
          this.errMsg[key] = `${key.charAt(0).toUpperCase() + key.slice(1)} must be at least ${
            control.errors['minlength'].requiredLength
          } characters.`;
        } else if (control.errors?.['pattern']) {
          this.errMsg[key] = this.getPatternErrorMessage(key);
        }
      }
    }

    return Object.keys(this.errMsg).length === 0;
  }

  getPatternErrorMessage(key: string): string {
    switch (key) {
      case 'name':
        return 'Name can only contain letters and spaces.';
      case 'username':
        return 'Username cannot contain spaces.';
      default:
        return 'Invalid input.';
    }
  }
  createUser(event: Event) {
    event.preventDefault();
    if (this.validateForm()) {
      const newUser = this.userForm.getRawValue();
      this.adminService.addUser(newUser).subscribe({
        next: () => {
          this.toastr.success('User created successfully!', 'Success');
          this.userCreated.emit();
          this.closeModal.emit();
        },
        error: (err) => {
          this.toastr.error(err.error?.errMsg, 'Error');
        }
      });
    }
  }
}
