import { Injectable } from '@angular/core';
import axios, { AxiosInstance} from 'axios';
import { environment } from '../../../environments/environment';
import { Router } from '@angular/router';
@Injectable({
  providedIn: 'root'
})
export class AxiosConfigService {
  private axiosInstance: AxiosInstance;
  constructor(private router: Router) {
    // Tạo instance của axios với config mặc định
    this.axiosInstance = axios.create({
      baseURL: environment.apiBaseUrl,
      headers: {
        'Content-Type': 'application/json'
      }
    });

    // Thêm request interceptor
    this.axiosInstance.interceptors.request.use(
      (config) => {
        const currentUser = localStorage.getItem('currentUser');
        if (currentUser) {
          const token = JSON.parse(currentUser).token;
          if (token) {
            config.headers.Authorization = `Bearer ${token}`;
          }
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // Thêm response interceptor
    this.axiosInstance.interceptors.response.use(
      (response) => {
        return response;
      },
      (error) => {
        if (error.response?.status === 401 || error.response?.status === 403) {
          localStorage.removeItem('currentUser');
          this.router.navigate(['/login']);
        }
        return Promise.reject(error);
      }
    );
  }

  // Getter để lấy instance đã được cấu hình
  get axios(): AxiosInstance {
    return this.axiosInstance;
  }
}
