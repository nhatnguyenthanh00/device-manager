import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../../../core/services/token.service';
import { HomePageService } from '../../../core/services/home-page.service';
import { STORAGE_KEYS, ROUTES } from '../../../core/constants';
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
    this.name = this.tokenService.getUserNameFromToken(localStorage.getItem(STORAGE_KEYS.TOKEN) as string || '');
  }
  onLogout(){
    localStorage.removeItem(STORAGE_KEYS.TOKEN);
    this.homePageService.resetActiveTab();
    this.router.navigate(['/'+ROUTES.LOGIN]);
  }
  goToProfile(){
    this.homePageService.setActiveTab('profile');
    const role = this.tokenService.getRoleFromToken(localStorage.getItem(STORAGE_KEYS.TOKEN) as string || '');
    if(role === 'ROLE_ADMIN') this.router.navigate(['/'+ROUTES.ADMIN_HOME]);
    else if(role == 'ROLE_USER') this.router.navigate(['/'+ROUTES.HOME]);
  }
}
