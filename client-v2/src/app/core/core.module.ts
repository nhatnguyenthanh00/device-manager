import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JwtInterceptor } from './interceptor/jwt.interceptor';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    HttpClientModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
       multi: true,
    },
  ],
})
export class CoreModule { }
