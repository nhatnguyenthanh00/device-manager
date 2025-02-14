import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../../../core/services/token.service';
import { HomePageService } from '../../../core/services/home-page.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit{
  @Output() tabChange = new EventEmitter<string>();
  constructor(private tokenService: TokenService, private homePageService: HomePageService, private router: Router) {

  }
  name: string|null = '';

  ngOnInit(): void {
    const token = localStorage.getItem('currentToken') === null ? '' : localStorage.getItem('currentToken')!;
    this.name = this.tokenService.getUserNameFromToken(token);
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
