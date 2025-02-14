import { Routes } from '@angular/router';
import { HelloWordComponent } from './hello-word/hello-word.component';
import { LoginComponent } from './auth/login/login.component';
// import { UserHomePageComponent } from './user/user-home-page/user-home-page.component';
// import { AdminHomePageComponent } from './admin/admin-home-page/admin-home-page.component';
export const routes: Routes = [
    {
        path: 'home',
        component: HelloWordComponent,
    },
    {
        path: 'login',
        component: LoginComponent,
    },
    {
        path: 'homepage',
        loadChildren: () => import('./user/user.module').then(m => m.UserModule)
    },
    {
        path: 'admin/homepage',
        loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule)
    },
    { path: '**', redirectTo: '/login' }
];
