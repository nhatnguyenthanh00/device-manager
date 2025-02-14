import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpService } from '../../core/services/http.service';
import { UserList } from '../../models/user-list.model';
import { NewUser } from '../../models/new-user.model';

@Injectable()
export class AdminService {
  constructor(private httpService: HttpService) {}

  /**
   * Retrieves a list of users.
   *
   * @param params - The parameters for querying users.
   * @returns An observable of the user list or null in case of an error.
   */
  getListUser(params: object): Observable<UserList> {
    // Make an HTTP GET request to fetch the user list
    return this.httpService.get<UserList>('/api/admin/user', params);
  }

  /**
   * Retrieves a user with the given user ID.
   *
   * @param userId - The ID of the user to retrieve.
   * @param page - The page number for pagination.
   * @returns An observable of the user details or null in case of an error.
   */
  getUser(userId: string, page: number): Observable<any> {
    // Make an HTTP POST request to fetch the user details
    return this.httpService.get<any>('/api/admin/user-detail', { userId: userId, page:page });
  }

  /**
   * Creates a new user.
   *
   * @param user - The new user to create.
   * @returns An observable of the created user or null in case of an error.
   */
  addUser(user: NewUser): Observable<any> {
    // Make an HTTP POST request to create a new user
    return this.httpService.post<any>('/api/admin/user', user);
  }

  /**
   * Updates a user.
   *
   * @param payload - The updated user details.
   * @returns An observable of the updated user or null in case of an error.
   */
  updateUser(payload: any): Observable<any> {
    // Make an HTTP PUT request to update a user
    return this.httpService.post<any>('/api/admin/user', payload);
  }

  /**
   * Resets the password for a user.
   *
   * @param payload - The payload of the request with the user ID and the new password.
   * @returns An observable of the response from the server or null in case of an error.
   */
  resetPassword(payload: any): Observable<any> {
    // Make an HTTP PUT request to reset a user's password
    return this.httpService.put<any>('/api/admin/reset-password', payload);
  }

  /**
   * Deletes a user.
   *
   * @param userId - The ID of the user to delete.
   * @returns An observable of the response from the server or null in case of an error.
   */
  deleteUser(userId: string): Observable<any> {
    // Make an HTTP DELETE request to delete a user
    return this.httpService.delete<any>('/api/admin/user', { id: userId });
  }

}
