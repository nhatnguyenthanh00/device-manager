import { Component } from '@angular/core';
import { DashboardConfig } from '../../models/dashboard.model';
import { SHARED_COMPONENT_REGISTRY } from '../../shared/component-registry';
@Component({
  selector: 'app-user-home-page',
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
        component: SHARED_COMPONENT_REGISTRY['userProfile'],
        hidden: true
      },
      {
        id: 'devices',
        label: 'My Devices',
        icon: 'fas fa-mobile-alt',
        component: SHARED_COMPONENT_REGISTRY['deviceManage'],
        inputs: {
          getAll: 'my-device'
        },
        hidden: true
      }
    ]
  };
}
