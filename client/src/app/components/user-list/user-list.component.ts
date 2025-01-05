import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../core/services/user.service';
import { CommonModule } from '@angular/common';
import { User } from '../../models/user.model';
@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent {
  users: User[] = [];
  loading = false;
  error: unknown = null;
  userService = inject(UserService);
  constructor() { 
    this.userService.getUsers().then(users => {
      this.users = users;
    });
  }
}
