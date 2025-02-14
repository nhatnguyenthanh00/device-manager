import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserHomePageComponent } from './user-home-page/user-home-page.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    UserHomePageComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
  ],
  exports: [
    UserHomePageComponent
  ]
})
export class UserModule { }
