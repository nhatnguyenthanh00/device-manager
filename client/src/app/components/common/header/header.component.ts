import { Component, Input, inject, OnInit, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../../../core/services/token.service';
import { HomePageService } from '../../../core/services/home-page.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  @Output() tabChange = new EventEmitter<string>();
  tokennService = inject(TokenService);
  router = inject(Router);
  homePageService = inject(HomePageService);
  // @Input() name !: string ;
  name: string|null = '';

  ngOnInit(): void {
    const token = localStorage.getItem('currentToken') === null ? '' : localStorage.getItem('currentToken')!;
    this.name = this.tokennService.getUserNameFromToken(token);
  }
  onLogout(){
    localStorage.removeItem('currentToken');
    this.router.navigate(['/login']);
  }
  goToProfile(){
    // this.router.navigate(['/profile']);
    this.homePageService.setActiveTab('profile');
  }
}
