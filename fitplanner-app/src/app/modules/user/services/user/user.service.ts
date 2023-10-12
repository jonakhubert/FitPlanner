import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap } from 'rxjs';
import { ChangePasswordRequest } from 'src/app/modules/user/interface/change-password-request';
import { ConfirmationResponse } from 'src/app/interface/confirmation-response';
import { User } from '../../interface/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly apiUrl = 'http://localhost:8222/api/users';

  constructor(private http: HttpClient) {}

  public getUser(email: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/get?email=${email}`)
  }

  public changePassword(request: ChangePasswordRequest): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/change-password`, request)
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
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/delete-account?email=${email}`, {})
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      }),
      tap(() => {
        localStorage.clear();
      })
    )
  }
}
