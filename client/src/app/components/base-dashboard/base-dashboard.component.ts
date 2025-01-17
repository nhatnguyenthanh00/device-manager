import { Component,Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardConfig, SidebarItem } from '../../models/dashboard.model';
import { HomePageService } from '../../core/services/home-page.service';
@Component({
  selector: 'app-base-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './base-dashboard.component.html',
  styleUrl: './base-dashboard.component.css'
})
export class BaseDashboardComponent {
  @Input() config!: DashboardConfig ;
  activeTab: string|null = null;

  constructor(private homePageService: HomePageService) {
    this.homePageService.activeTab$.subscribe(tabId => {
      this.activeTab = tabId;
    })
  }
  ngOnInit() {
  }

}
