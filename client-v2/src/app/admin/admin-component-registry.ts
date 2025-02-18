import { Type } from '@angular/core';
import { UserManageComponent } from './user-manage/user-manage.component';
export const ADMIN_COMPONENT_REGISTRY: Record<string, Type<any>> = {
    userManage: UserManageComponent,
  };