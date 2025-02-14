import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AdminService } from '../services/admin.service';
import { HomePageService } from '../../core/services/home-page.service';
import { User } from '../../models/user.model';
import { NewUser } from '../../models/new-user.model';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-user-manage',
  templateUrl: './user-manage.component.html',
  styleUrl: './user-manage.component.css'
})
export class UserManageComponent {

  constructor(private adminService: AdminService, private toastr: ToastrService, private homePageService: HomePageService) {}

  users: User[] = [];
  search: string = '';
  genderFilter: string = '';
  currentPage: number = 1;
  totalPages: number = 1;
  totalItems: number = 0;
  showCreateUserModal: boolean = false;
  newUser: Partial<NewUser> = {
    name: '',
    username: '',
    gender: '',
    password: '',
  };
  errMsg: string = '';


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
    console.log('View details for user:', user);
    this.homePageService.setActiveTab('user-detail');
    this.homePageService.setDetailUserId(user.userId);
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
    this.newUser = {
      name: '',
      username: '',
      gender: '',
      password: '',
    };
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
    this.newUser.password = password.sort(() => Math.random() - 0.5).join('');
  }

  validateInput(): boolean {
    if (!this.newUser.name) {
      this.errMsg = 'Name is required';
      return false;
    } else {
      const trimmedName = this.newUser.name.trim();
      const nameRegex = /^[\p{L}\s]+$/u;
      if (!nameRegex.test(trimmedName)) {
        this.errMsg = 'Name can only contain letters and spaces';
        return false;
      }
      this.newUser.name = trimmedName;
    }
    if (!this.newUser.username) {
      this.errMsg = 'Username is required';
      return false;
    } else {
      if (this.newUser.username.length < 6) {
        this.errMsg = 'Username must be at least 6 characters';
        return false;
      }
      if (this.newUser.username.includes(' ')) {
        this.errMsg = 'Username cannot contain spaces';
        return false;
      }
    }

    if (!this.newUser.password) {
      this.errMsg = 'Password is required';
      return false;
    }

    // Gender validation
    if (!this.newUser.gender) {
      this.errMsg = 'Gender is required';
      return false;
    }

    return true;
  }

  createUser(event: Event) {
    event.preventDefault();
    if (this.validateInput()) {

      this.adminService.addUser(this.newUser as NewUser).subscribe({
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
