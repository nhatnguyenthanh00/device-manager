import { Injectable } from '@angular/core';
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';
  constructor()  { }

  async getUsers() {
    try{
      const response = await fetch(this.apiUrl);
      return response.json();
    } catch (error) {
      console.error(error);
      return [];
    }
  }
}
