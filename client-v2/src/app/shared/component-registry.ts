import { Type } from '@angular/core';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { DeviceManageComponent } from './components/device-manage/device-manage.component';

export const SHARED_COMPONENT_REGISTRY: Record<string, Type<any>> = {
  userProfile: UserProfileComponent,
  deviceManage: DeviceManageComponent,
};
