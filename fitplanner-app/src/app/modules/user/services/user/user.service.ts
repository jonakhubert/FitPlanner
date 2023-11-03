import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap } from 'rxjs';
import { ConfirmationResponse } from 'src/app/interface/confirmation-response';
import { User } from '../../interface/user';
import { UserDetailsRequest } from '../../interface/user-details-request';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly apiUrl = 'http://localhost:8222/api/user-management';

  constructor(private http: HttpClient) {}

  public getUser(email: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/${email}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public changePassword(email: string, request: string): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/users/${email}/password-change`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      }),
      tap(() => {
        localStorage.clear();
      })
    );
  }

  public deleteAccount(email: string): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/users/${email}/account-deletion`, {})
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      }),
      tap(() => {
        localStorage.clear();
      })
    )
  }

  public updateUserDetails(email: string, request: UserDetailsRequest): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/users/${email}/details`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }
}
