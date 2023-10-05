import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { DailyMealPlan } from '../../interface/daily-meal-plan';
import { MealRequest } from '../../interface/meal-request';
import { ConfirmationResponse } from 'src/app/interface/confirmation-response';

@Injectable({
  providedIn: 'root'
})
export class NutritionService {

  private readonly apiUrl = 'http://localhost:8222/api/nutrition';

  constructor(private http: HttpClient) {}

  public getDailyMealPlan(email: string, date: string): Observable<DailyMealPlan> {
    return this.http.get<DailyMealPlan>(`${this.apiUrl}/daily-meal-plan?email=${email}&date=${date}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public addFoodItem(request: MealRequest): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/add-food-item`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public removeFoodItem(request: MealRequest): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/remove-food-item`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }
}
