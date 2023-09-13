import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap } from 'rxjs';
import { RegisterRequest } from '../interface/register-request';
import { LoginResponse } from '../interface/login-response';
import { LoginRequest } from '../interface/login-request';
import { ApiError } from '../interface/api-error';
import { ConfirmationResponse } from '../interface/confirmation-response';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private readonly apiUrl = 'http://localhost:8222/api/auth';

  constructor(private http: HttpClient) {}

  public register(request: RegisterRequest): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/register`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw this.buildApiError(error);
      })
    );
  }

  public login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw this.buildApiError(error);
      }),
      tap((response: LoginResponse) => {
        localStorage.setItem('userEmail', request.email);
        localStorage.setItem('token', response.access_token);
      })
    );
  }

  public logout(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/logout`)
    .pipe(
      tap(() => {
        localStorage.clear();
      })
    )
  }

  public isLoggedIn() {
    return localStorage.getItem('token') !== null;
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
