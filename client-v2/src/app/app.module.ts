import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { routes } from './app.routes';
import { HelloWordModule } from './hello-word/hello-word.module';
import { AuthModule } from './auth/auth.module';
import { HttpClientModule } from '@angular/common/http';
import { UserModule } from './user/user.module';
import { AdminModule } from './admin/admin.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { CoreModule } from './core/core.module';
@NgModule({
  declarations: [AppComponent],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HelloWordModule,
    AuthModule,
    UserModule,
    AdminModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
        positionClass: 'toast-top-right',
        timeOut: 5000,
        closeButton: true,
        progressBar: true,
    }),
    CoreModule
  ],
  providers: [],
  exports: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
