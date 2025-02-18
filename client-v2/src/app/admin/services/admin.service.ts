import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpService } from '../../core/services/http.service';
import { API_ENDPOINT } from '../../core/constants';
@Injectable()
export class AdminService {
  constructor(private httpService: HttpService) {}

  /**
   * Retrieves a list of users.
   *
   * @param params - The parameters for querying users.
   * @returns An observable of the user list or null in case of an error.
   */
  getListUser(params: object): Observable<any> {
    // Make an HTTP GET request to fetch the user list
    return this.httpService.get<any>(API_ENDPOINT.USER, params);
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
    return this.httpService.get<any>(API_ENDPOINT.USER_DETAIL, { userId: userId, page:page });
  }

  /**
   * Creates a new user.
   *
   * @param user - The new user to create.
   * @returns An observable of the created user or null in case of an error.
   */
  addUser(user: any): Observable<any> {
    // Make an HTTP POST request to create a new user
    return this.httpService.post<any>(API_ENDPOINT.USER, user);
  }

  /**
   * Updates a user.
   *
   * @param payload - The updated user details.
   * @returns An observable of the updated user or null in case of an error.
   */
  updateUser(payload: any): Observable<any> {
    // Make an HTTP PUT request to update a user
    return this.httpService.post<any>(API_ENDPOINT.USER, payload);
  }

  /**
   * Resets the password for a user.
   *
   * @param payload - The payload of the request with the user ID and the new password.
   * @returns An observable of the response from the server or null in case of an error.
   */
  resetPassword(payload: any): Observable<any> {
    // Make an HTTP PUT request to reset a user's password
    return this.httpService.put<any>(API_ENDPOINT.RESET_PASWORD, payload);
  }

  /**
   * Deletes a user.
   *
   * @param userId - The ID of the user to delete.
   * @returns An observable of the response from the server or null in case of an error.
   */
  deleteUser(userId: string): Observable<any> {
    // Make an HTTP DELETE request to delete a user
    return this.httpService.delete<any>(API_ENDPOINT.USER, { id: userId });
  }

}
