import { Component, Input, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  router = inject(Router); 
  @Input() name !: string ;
  onLogout(){
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }
  goToProfile(){
    this.router.navigate(['/profile']);
  }
}
