import { Component, Input } from '@angular/core';
import { DashboardConfig } from '../../../models/dashboard.model';
import { HomePageService } from '../../../core/services/home-page.service';
@Component({
  selector: 'app-base-dashboard',
  templateUrl: './base-dashboard.component.html',
  styleUrl: './base-dashboard.component.css'
})
export class BaseDashboardComponent {
  @Input() config!: DashboardConfig ;
  activeTab: string|null = null;

  constructor(public homePageService: HomePageService) {
    // this.homePageService.activeTab$.subscribe(tabId => {
    //   console.log('In BaseDashboardComponent, tabId:', tabId);
    //   this.activeTab = tabId;
    // })
  }

  activeTab$ = this.homePageService.activeTab$;
}
