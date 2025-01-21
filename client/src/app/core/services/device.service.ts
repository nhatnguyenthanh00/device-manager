import { Injectable } from '@angular/core';
import { catchError, of, Observable } from 'rxjs';
import { HttpService } from './http.service';
import { NewDevice } from '../../models/new-device.model';
@Injectable({
  providedIn: 'root'
})
export class DeviceService {

  constructor(private httpService: HttpService) {}
  getAllDevice(params : object) {
    return this.httpService.get<any>('/api/admin/device', params).pipe(
          catchError((error) => {
            console.error('Error fetching device list:', error);
            return of(null);
          })
        );
  }

  addDevice(device: NewDevice): Observable<any> {
    return this.httpService.post<any>('/api/admin/device', device);
  }

  updateDevice(device: object): Observable<any> {
    return this.httpService.put<any>('/api/admin/device', device);
  }

  deleteDevice(deviceId: string): Observable<any> {
    return this.httpService.delete<any>('/api/admin/device',{id:deviceId});
  }
}
