import { Component} from '@angular/core';
import { HeaderComponent } from '../../common/header/header.component';
import { UserManageComponent } from '../user-manage/user-manage.component';
import { UserDetailManageComponent } from '../user-detail-manage/user-detail-manage.component';
import { DeviceManageComponent } from '../device-manage/device-manage.component';
import { UserProfileComponent } from '../../common/user-profile/user-profile.component';
import { CommonModule } from '@angular/common';
import { BaseDashboardComponent } from '../../common/base-dashboard/base-dashboard.component';
import { DashboardConfig } from '../../../models/dashboard.model';
@Component({
  selector: 'app-admin-home-page',
  standalone: true,
  imports: [CommonModule, HeaderComponent, BaseDashboardComponent],
  templateUrl: './admin-home-page.component.html',
  styleUrl: './admin-home-page.component.css',
})
export class AdminHomePageComponent {
  activeTab: 'user' | 'device' | null = null;
  dashboardConfig: DashboardConfig = {
    title: 'Admin Panel',
    sidebarItems: [
      {
        id: 'profile',
        label: 'My Profile',
        icon: 'fas fa-user',
        component: UserProfileComponent,
        hidden: true
      },
      {
        id: 'user',
        label: 'User Manage',
        icon: 'fas fa-users',
        component: UserManageComponent,
        hidden: true
      },
      {
        id: 'device',
        label: 'Device Manage',
        icon: 'fas fa-mobile-alt',
        component: DeviceManageComponent,
        hidden: true,
        inputs: {
          getAll: true
        }
      },
      {
        id: 'user-detail',
        component: UserDetailManageComponent,
        hidden: false
      }
    ],
  };
}
