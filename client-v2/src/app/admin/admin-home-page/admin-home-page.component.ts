import { Component } from '@angular/core';
import { DashboardConfig } from '../../models/dashboard.model';
import { SHARED_COMPONENT_REGISTRY } from '../../shared/component-registry';
import { ADMIN_COMPONENT_REGISTRY } from '../admin-component-registry';
// import { UserProfileComponent } from '../../shared/components/user-profile/user-profile.component';
// import { DeviceManageComponent } from '../../shared/components/device-manage/device-manage.component';
@Component({
  selector: 'app-admin-home-page',
  templateUrl: './admin-home-page.component.html',
  styleUrl: './admin-home-page.component.css'
})
export class AdminHomePageComponent {
  dashboardConfig: DashboardConfig = {
    title: 'Admin Panel',
    sidebarItems: [
      {
        id: 'profile',
        label: 'My Profile',
        icon: 'fas fa-user',
        component: SHARED_COMPONENT_REGISTRY['userProfile'],
        hidden: true
      },
      {
        id: 'device',
        label: 'Device Manage',
        icon: 'fas fa-mobile-alt',
        component: SHARED_COMPONENT_REGISTRY['deviceManage'],
        hidden: true,
        inputs: {
          getAll: 'device-manager'
        }
      },
      {
        id: 'user',
        label: 'User Manage',
        icon: 'fas fa-users',
        component: ADMIN_COMPONENT_REGISTRY['userManage'],
        hidden: true
      },
      {
        id: 'user-detail',
        component: ADMIN_COMPONENT_REGISTRY['userDetailManage'],
        hidden: false
      }
    ],
  };
}
