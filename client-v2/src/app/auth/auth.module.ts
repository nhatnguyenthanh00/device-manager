import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { AuthService } from './services/auth.service';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    CommonModule,
    FormsModule
  ],
  providers: [
    AuthService
  ],
  exports: [
    LoginComponent
  ]
})
export class AuthModule { }
