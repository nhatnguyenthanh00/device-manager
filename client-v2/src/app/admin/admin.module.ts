import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../shared/shared.module';
import { AdminHomePageComponent } from './admin-home-page/admin-home-page.component';

import { AdminService } from './services/admin.service';
import { UserManageComponent } from './user-manage/user-manage.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { CreateUserComponent } from './create-user/create-user.component';


@NgModule({
  declarations: [
    AdminHomePageComponent,
    UserManageComponent,
    UserDetailComponent,
    CreateUserComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [AdminService]
})
export class AdminModule { }
