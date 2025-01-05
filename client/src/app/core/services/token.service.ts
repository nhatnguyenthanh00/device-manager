import { Injectable } from '@angular/core';
import {jwtDecode} from "jwt-decode";
@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor() { }
  decodeToken(token: string): any {
    try {
      return jwtDecode(token);
    } catch (error) {
      return null;
    }
  }

  isTokenValid(token: string): boolean {
    try {
      const decodedToken: { exp: number } = jwtDecode(token);
      const currentTime = Math.floor(Date.now() / 1000); // Lấy thời gian hiện tại (giây)
      return decodedToken.exp > currentTime; // Token còn hạn
    } catch (e) {
      return false; // Token không hợp lệ
    }
  }
}
