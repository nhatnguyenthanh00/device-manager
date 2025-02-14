import { Routes } from '@angular/router';
import { HelloWordComponent } from './shared/components/hello-word/hello-word.component';
import { LoginComponent } from './auth/login/login.component';
import { ErrorPageComponent } from './shared/components/error-page/error-page.component';
import { authGuard } from './core/guards/auth.guard';
import { AdminHomePageComponent } from './admin/admin-home-page/admin-home-page.component';
import { UserHomePageComponent } from './user/user-home-page/user-home-page.component';
import {ROUTES} from './core/constants';
export const routes: Routes = [
    {
        path: ROUTES.FORBIDDEN,
        component: ErrorPageComponent,
        data:{
            notFoundData: {
                code : '403',
                title: 'ACCESS DENIED',
                message: 'Xin lỗi, bạn không có quyền truy cập trang này!'
            }
        }
    },
    {   
        path: ROUTES.LOGIN,
        component: LoginComponent,
    },
    {   
        path: ROUTES.HOME,
        component: UserHomePageComponent,
        // loadChildren: () => import('./user/user.module').then(m => m.UserModule),
        canActivate: [authGuard]
    },
    {   
        path: ROUTES.ADMIN_HOME,
        component: AdminHomePageComponent,
        // loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule),
        canActivate: [authGuard], data: { role: 'ROLE_ADMIN' }
    },
    {
        path: '',
        component: HelloWordComponent
    },
    {
        path : '**',
        component: ErrorPageComponent,
        data:{
            notFoundData: {
                code : '404',
                title: 'PAGE NOT FOUND',
                message: 'Xin lỗi, trang bạn đang tìm kiếm không tồn tại!'
            }
        }
    }
];
