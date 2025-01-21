import { Routes } from '@angular/router';
import { LoginComponent } from './components/common/login/login.component';
import { AdminHomePageComponent } from './components/admin/admin-home-page/admin-home-page.component';
import { UserHomePageComponent } from './components/user/user-home-page/user-home-page.component';
import { authGuard } from './core/guards/auth.guard';
export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'admin/homepage', component: AdminHomePageComponent, canActivate: [authGuard], data: { role: 'ROLE_ADMIN' } },
    { path: 'homepage', component: UserHomePageComponent, canActivate: [authGuard] },
    { path: '**', redirectTo: '/login' }
];
