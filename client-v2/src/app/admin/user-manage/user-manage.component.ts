import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AdminService } from '../services/admin.service';
import { HomePageService } from '../../core/services/home-page.service';
import { User } from '../../models/user.model';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-user-manage',
  templateUrl: './user-manage.component.html',
  styleUrl: './user-manage.component.css'
})
export class UserManageComponent {

  constructor(private adminService: AdminService, private toastr: ToastrService, private homePageService: HomePageService) {}
  router = inject(Router);
  users: User[] = [];
  search: string = '';
  genderFilter: string = '';
  currentPage: number = 1;
  totalPages: number = 1;
  totalItems: number = 0;
  showCreateUserModal: boolean = false;
  userForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[\p{L}\s]+$/u) // Chỉ cho phép chữ cái và khoảng trắng
    ]),
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
      Validators.pattern(/^\S+$/) // Không chứa khoảng trắng
    ]),
    gender: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required) // Chỉ đọc
  });

  errMsg: { [key: string]: string } = {};

  ngOnInit() {
    this.getUsers();
  }

  getUsers() {
    const params = {
      search: this.search,
      gender: this.genderFilter,
      page: this.currentPage,
    };

    this.adminService.getListUser(params).subscribe({
      next: (res) => {
        this.users = res?.content; // Adjust depending on your response structure
        this.totalPages = res?.totalPages; // Adjust depending on your response structure
        this.totalItems = res?.totalItems;
      },
      error: (err) => {
        this.toastr.error(err.error?.errMsg, 'Error');
        this.users = [];
        this.totalPages = 1;
        this.totalItems = 0;
      },
    });
  }

  onSearch() {
    this.currentPage = 1; // Reset to the first page when the search changes
    this.getUsers();
  }

  onGenderFilterChange() {
    this.currentPage = 1; // Reset to the first page when the gender filter changes
    this.getUsers();
  }

  previousPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.getUsers();
    }
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.getUsers();
    }
  }

  viewDetails(user: User) {
    // Navigate to user detail page (use Angular Router)
    // this.homePageService.setActiveTab('user-detail');
    // this.homePageService.setDetailUserId(user.userId);
    // window.location.href = '/admin/user-detail';
    this.router.navigate(['/admin/user-detail/' + user.userId]);
  }

  deleteUser(userId: string, userName: string) {
    Swal.fire({
      title: 'Are you sure?',
      text: `Do you want to delete user "${userName}"?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel',
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
    }).then((result) => {
      if (result.isConfirmed) {

        this.adminService.deleteUser(userId).subscribe({
          next: (response) => {
            this.toastr.success(`Delete user ${userName} succes!`, 'Success');
            this.currentPage = 1;
            this.getUsers();
          },
          error: (err) => {
            this.toastr.error(err.error?.errMsg, 'Error');
          },
        });
      }
    });
  }

  onCreateNewUser() {
    this.showCreateUserModal = true;
    this.userForm.reset();
    this.errMsg = {};
  }
  closeModal() {
    this.showCreateUserModal = false;
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
    if(this.validateForm()){
      const newUser = this.userForm.getRawValue();
      console.log('Creating user:', newUser);
      this.adminService.addUser(newUser).subscribe({
        next: (response) => {
          this.toastr.success('User created successfully!', 'Success');
          this.getUsers();
          this.closeModal();
        },
        error: (err) => {
          this.toastr.error(err.error?.errMsg, 'Error');
        },
      });
    }
  }
  
  canDelete(user: User): boolean {
    // Điều kiện để cho phép xóa, ví dụ: chỉ Admin mới được xóa
    return user.totalDevice === 0;  
  }

}
