import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
@Component({
  selector: 'app-admin-home-page',
  standalone: true,
  imports: [HeaderComponent],
  templateUrl: './admin-home-page.component.html',
  styleUrl: './admin-home-page.component.css'
})
export class AdminHomePageComponent {
  constructor() {
    const storedUser = localStorage.getItem('currentUser');
    let currentUser = null;
    if (storedUser !== null) {
      currentUser = JSON.parse(storedUser);
    }
  }
}
