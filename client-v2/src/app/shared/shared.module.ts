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
import { HelloWordComponent } from './components/hello-word/hello-word.component';

@NgModule({
  declarations: [
    HeaderComponent,
    BaseDashboardComponent,
    UserProfileComponent,
    DeviceManageComponent,
    ErrorPageComponent,
    HelloWordComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ReactiveFormsModule
  ],
  exports: [
    HeaderComponent,
    BaseDashboardComponent,
    UserProfileComponent,
    DeviceManageComponent
  ],
  providers: [DeviceService]
})
export class SharedModule { }
