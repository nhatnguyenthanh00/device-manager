import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { BaseDashboardComponent } from '../base-dashboard/base-dashboard.component';
import { UserProfileComponent } from '../user-profile/user-profile.component';
import { DeviceManageComponent } from '../device-manage/device-manage.component';
import { DashboardConfig } from '../../models/dashboard.model';
@Component({
  selector: 'app-user-home-page',
  standalone: true,
  imports: [HeaderComponent,BaseDashboardComponent],
  templateUrl: './user-home-page.component.html',
  styleUrl: './user-home-page.component.css'
})
export class UserHomePageComponent {
  dashboardConfig: DashboardConfig = {
    title: 'User Panel',
    sidebarItems: [
      {
        id: 'profile',
        label: 'My Profile',
        icon: 'fas fa-user',
        component: UserProfileComponent
      },
      {
        id: 'devices',
        label: 'My Devices',
        icon: 'fas fa-mobile-alt',
        component: DeviceManageComponent
      }
    ]
  };
}
