import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../core/services/user.service';
import { User } from '../../models/user.model';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../core/services/admin.service';
@Component({
  selector: 'app-user-manage',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-manage.component.html',
  styleUrl: './user-manage.component.css'
})
export class UserManageComponent {
  fakeUsers = [
    { id: 1, name: 'John Doe', username: 'johndoe', gender: 'Male' },
    { id: 2, name: 'Jane Smith', username: 'janesmith', gender: 'Female' },
  ];

  users: User[] = [];
  search: string = '';
  genderFilter: string = '';
  currentPage: number = 1;
  totalPages: number = 1;

  constructor(private userService: UserService, private adminService: AdminService) {}

  ngOnInit() {
    this.getUsers();
  }

  getUsers() {
    const params = {
      search: this.search,
      gender: this.genderFilter,
      page: this.currentPage,
    };

    this.adminService.getListUser().subscribe(response => {
      console.log('In user-manage component' + response);
      this.users = response;  // Adjust depending on your response structure
      this.totalPages = 1;  // Adjust depending on your response structure
    });
  
    // this.users = this.fakeUsers;
    // this.totalPages = 1;
    // this.userService.getUsers(params).subscribe(response => {
    //   this.users = response.data;  // Adjust depending on your response structure
    //   this.totalPages = response.totalPages;  // Adjust depending on your response structure
    // });
  }

  onSearch() {
    this.currentPage = 1;  // Reset to the first page when the search changes
    this.getUsers();
  }

  onGenderFilterChange() {
    this.currentPage = 1;  // Reset to the first page when the gender filter changes
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
  }

  deleteUser(userId: string) {
    // if (confirm('Are you sure you want to delete this user?')) {
    //   this.userService.deleteUser(userId).subscribe(() => {
    //     this.getUsers();  // Refresh the list after deletion
    //   });
    // }
  }

  onCreateNewUser() {
    // Navigate to user creation form
    console.log('Navigate to user creation form');
  }
}
