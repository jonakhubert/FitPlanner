import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap } from 'rxjs';
import { ApiError } from 'src/app/interface/api-error';
import { ChangePasswordRequest } from 'src/app/modules/user/interface/change-password-request';
import { ConfirmationResponse } from 'src/app/interface/confirmation-response';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly apiUrl = 'http://localhost:8222/api/user';

  constructor(private http: HttpClient) {}

  public changePassword(request: ChangePasswordRequest): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/change-password`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw this.buildApiError(error);
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
        throw this.buildApiError(error);
      }),
      tap(() => {
        localStorage.clear();
      })
    )
  }

  private buildApiError(error: HttpErrorResponse) {
    const apiError: ApiError = {
      path: error.error.path,
      message: error.error.message,
      statusCode: error.error.statusCode,
      time: error.error.timestamp
    };

    return apiError;
  }
}
