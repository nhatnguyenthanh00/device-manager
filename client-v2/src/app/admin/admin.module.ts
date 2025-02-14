import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { AdminHomePageComponent } from './admin-home-page/admin-home-page.component';

import { AdminService } from './services/admin.service';
import { UserManageComponent } from './user-manage/user-manage.component';

import { routes } from './admin-routing.module';
import { UserDetailManageComponent } from './user-detail-manage/user-detail-manage.component';


@NgModule({
  declarations: [
    AdminHomePageComponent,
    UserManageComponent,
    UserDetailManageComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule,
    RouterModule.forChild(routes)
  ],
  providers: [AdminService]
})
export class AdminModule { }
