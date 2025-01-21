import { Component } from '@angular/core';
import { HeaderComponent } from '../../common/header/header.component';
import { BaseDashboardComponent } from '../../common/base-dashboard/base-dashboard.component';
import { UserProfileComponent } from '../../common/user-profile/user-profile.component';
import { DeviceManageComponent } from '../../admin/device-manage/device-manage.component';
import { DashboardConfig } from '../../../models/dashboard.model';
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
        component: UserProfileComponent,
        hidden: true
      },
      {
        id: 'devices',
        label: 'My Devices',
        icon: 'fas fa-mobile-alt',
        component: DeviceManageComponent,
        hidden: true
      }
    ]
  };
}
