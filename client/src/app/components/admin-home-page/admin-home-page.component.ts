import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { UserManageComponent } from '../user-manage/user-manage.component';
import { DeviceManageComponent } from '../device-manage/device-manage.component';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-admin-home-page',
  standalone: true,
  imports: [CommonModule,HeaderComponent, UserManageComponent, DeviceManageComponent],
  templateUrl: './admin-home-page.component.html',
  styleUrl: './admin-home-page.component.css'
})
export class AdminHomePageComponent {

  activeTab: 'user' | 'device' = 'user';

}
