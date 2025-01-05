import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { authGuard } from './core/guards/auth.guard';
export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'admin', component: UserListComponent, canActivate: [authGuard], data: { role: 'ROLE_ADMIN' } },
    { path: '**', redirectTo: '/login' }
];
