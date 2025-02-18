import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { CheckLoginComponent } from './shared/components/check-login/check-login.component';
import { ErrorPageComponent } from './shared/components/error-page/error-page.component';
import { authGuard } from './core/guards/auth.guard';
import { AdminHomePageComponent } from './admin/admin-home-page/admin-home-page.component';
import { UserHomePageComponent } from './user/user-home-page/user-home-page.component';
import { UserDetailComponent } from './admin/user-detail/user-detail.component';
import { ROUTES } from './core/constants';
export const routes: Routes = [
  {
    path: ROUTES.FORBIDDEN,
    component: ErrorPageComponent,
    data: {
      notFoundData: {
        code: '403',
        title: 'ACCESS DENIED',
        message: 'Xin lỗi, bạn không có quyền truy cập trang này!',
      },
    },
  },
  {
    path: ROUTES.LOGIN,
    component: LoginComponent,
  },
  {
    path: ROUTES.HOME,
    component: UserHomePageComponent,
    canActivate: [authGuard],
  },
  {
    path: ROUTES.ADMIN_HOME,
    component: AdminHomePageComponent,
    canActivate: [authGuard],
    data: { role: 'ROLE_ADMIN' },
  },
  {
    path: 'admin/user-detail/:userId',
    component: UserDetailComponent,
    canActivate: [authGuard],
    data: { role: 'ROLE_ADMIN' },
  },
  { path: '', redirectTo: '/check-login', pathMatch: 'full' },
  { path: 'check-login', component: CheckLoginComponent },
  {
    path: '**',
    component: ErrorPageComponent,
    data: {
      notFoundData: {
        code: '404',
        title: 'PAGE NOT FOUND',
        message: 'Xin lỗi, trang bạn đang tìm kiếm không tồn tại!',
      },
    },
  },
];
