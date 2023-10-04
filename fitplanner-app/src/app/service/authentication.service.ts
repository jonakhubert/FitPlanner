import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap } from 'rxjs';
import { RegisterRequest } from '../interface/register-request';
import { LoginResponse } from '../interface/login-response';
import { LoginRequest } from '../interface/login-request';
import { ConfirmationResponse } from '../interface/confirmation-response';
import { ResetPasswordRequest } from '../interface/reset-password-request';

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
        throw error;
      })
    );
  }

  public login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
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
      catchError((error: HttpErrorResponse) => {
        throw error;
      }),
      tap(() => {
        localStorage.clear();
      })
    )
  }

  public forgotPassword(email: string): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/forgot-password?email=${email}`, {})
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    );
  }

  public resetPassword(request: ResetPasswordRequest): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/reset-password`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    );
  }

  public validateResetPasswordToken(token: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/validate-reset-password-token`, {}, {
      headers: {
        'X-Reset-Password-Token': token,
      },
    });
  }

  public authorize(): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/validate-access-token`, {});
  }

  public isLoggedIn() {
    return localStorage.getItem('token') !== null;
  }
}
