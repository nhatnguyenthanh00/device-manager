import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AdminService } from '../services/admin.service';
import { User } from '../../models/user.model';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-user-manage',
  templateUrl: './user-manage.component.html',
  styleUrl: './user-manage.component.css'
})
export class UserManageComponent {

  constructor(private adminService: AdminService, private toastr: ToastrService, private router: Router) {}
  users: User[] = [];
  search: string = '';
  genderFilter: string = '';
  currentPage: number = 1;
  totalPages: number = 1;
  totalItems: number = 0;
  showCreateUserModal: boolean = false;

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

  
  onCreateNewUser() {
    this.showCreateUserModal = true;
  }

  closeCreateUserModal() {
    this.showCreateUserModal = false;
  }

  viewDetails(user: User) {
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
  
  canDelete(user: User): boolean {
    // Điều kiện để cho phép xóa
    return user.totalDevice === 0;  
  }

}
