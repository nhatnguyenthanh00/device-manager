import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HeaderComponent } from './components/header/header.component';
import { BaseDashboardComponent } from './components/base-dashboard/base-dashboard.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { DeviceManageComponent } from './components/device-manage/device-manage.component';
import { DeviceService } from './services/device.service';
import { ErrorPageComponent } from './components/error-page/error-page.component';
import { CheckLoginComponent } from './components/check-login/check-login.component';
import { CreateDeviceComponent } from './components/create-device/create-device.component';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
@NgModule({
  declarations: [
    HeaderComponent,
    BaseDashboardComponent,
    UserProfileComponent,
    DeviceManageComponent,
    ErrorPageComponent,
    CheckLoginComponent,
    CreateDeviceComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule
  ],
  exports: [
    HeaderComponent,
    BaseDashboardComponent,
    UserProfileComponent,
    DeviceManageComponent,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule
  ],
  providers: [DeviceService]
})
export class SharedModule { }
