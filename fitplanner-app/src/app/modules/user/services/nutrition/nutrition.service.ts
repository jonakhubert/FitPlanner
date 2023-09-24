import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { ApiError } from 'src/app/interface/api-error';

@Injectable({
  providedIn: 'root'
})
export class NutritionService {

  private readonly apiUrl = 'http://localhost:8222/api/nutrition';

  constructor(private http: HttpClient) {}

  public hello(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/hello`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw this.buildApiError(error);
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
