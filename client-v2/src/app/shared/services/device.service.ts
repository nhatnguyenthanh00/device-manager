import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpService } from '../../core/services/http.service';
import { API_ENDPOINT } from '../../core/constants';
@Injectable()
export class DeviceService {

  constructor(private httpService: HttpService) {}

  /**
   * Fetches all devices based on provided parameters.
   *
   * @param params - The parameters for querying devices.
   * @returns An observable of device data or null in case of an error.
   */
  getAllDevice(params: object): Observable<any> {
    // Make an HTTP GET request to fetch all devices
    return this.httpService.get<any>(API_ENDPOINT.DEVICE, params);
  }

  /**
   * Fetches all devices owned by the current user based on provided parameters.
   *
   * @param params - The parameters for querying devices.
   * @returns An observable of device data or null in case of an error.
   */
  getMyDevice(params: object): Observable<any> {
    // Make an HTTP GET request to fetch all devices owned by the current user
    return this.httpService.get<any>(API_ENDPOINT.MY_DEVICE, params);
  }

  /**
   * Creates a new device and assigns it to the current user.
   *
   * @param device - The new device to be created.
   * @returns An observable of the created device data or null in case of an error.
   */
  addDevice(device: object): Observable<any> {
    return this.httpService.post<any>(API_ENDPOINT.DEVICE, device);
  }

  /**
   * Updates an existing device with the provided information.
   *
   * @param device - The device object with updated information.
   * @returns An observable of the updated device data or null in case of an error.
   */
  updateDevice(device: object): Observable<any> {
    return this.httpService.post<any>(API_ENDPOINT.DEVICE, device);
  }

  /**
   * Deletes a device with the given ID.
   *
   * @param deviceId - The ID of the device to be deleted.
   * @returns An observable of the deleted device data or null in case of an error.
   */
  deleteDevice(deviceId: string): Observable<any> {
    return this.httpService.delete<any>(API_ENDPOINT.DEVICE,  { id: deviceId } );
  }

  /**
   * Requests to return a device to the admin.
   *
   * @param deviceId The ID of the device to be returned.
   * @returns An observable of the request result.
   *
   * This method sends a POST request to the /api/device-return endpoint with the
   * device ID as a parameter. The request is expected to return a JSON response
   * with a success message if the request is processed successfully.
   */
  requestReturnDevice(deviceId: string): Observable<any> {
    return this.httpService.post<any>(API_ENDPOINT.DEVICE_RETURN,{}, { id: deviceId });
  }

  /**
   * Accepts a device return request from a user.
   *
   * @param deviceId The ID of the device to be accepted.
   * @returns An observable of the request result.
   *
   * This method sends a POST request to the /api/admin/accept-return endpoint with the
   * device ID as a parameter. The request is expected to return a JSON response
   * with a success message if the request is processed successfully.
   */
  acceptReturnDevice(deviceId: string): Observable<any> {
    return this.httpService.post<any>(API_ENDPOINT.ACCEPT_RETUEN,{}, { id: deviceId });
  }

  refuseReturnDevice(deviceId: string): Observable<any> {
    return this.httpService.post<any>(API_ENDPOINT.REFUSE_RETURN,{}, { id: deviceId });
  }

  /**
   * Retrieves a list of all usernames.
   *
   * @returns An observable of the response from the server or null in case of an error.
   */
  getUsernames(): Observable<any> {
    // Make an HTTP GET request to fetch all usernames
    return this.httpService.get<any>(API_ENDPOINT.USER_SELECT);
  }
}

