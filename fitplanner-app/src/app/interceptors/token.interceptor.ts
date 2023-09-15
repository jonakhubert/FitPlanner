import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, catchError } from 'rxjs';
import { Router } from '@angular/router';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(
    private router: Router
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = localStorage.getItem('token');

    if(token) {
      request = request.clone({
        setHeaders: {Authorization: `Bearer ${token}`}
      })
    }

    return next.handle(request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.clear();
          this.router.navigate(['login']);
        }
          
        throw error;
      })
    );
  }
}
