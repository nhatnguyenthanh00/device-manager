import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserHomePageComponent } from './user-home-page/user-home-page.component';
import { SharedModule } from '../shared/shared.module';
import { RouterModule } from '@angular/router';
import { routes } from './user-routing.module';

@NgModule({
  declarations: [
    UserHomePageComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes)
  ],
  exports: [
    UserHomePageComponent
  ]
})
export class UserModule { }
