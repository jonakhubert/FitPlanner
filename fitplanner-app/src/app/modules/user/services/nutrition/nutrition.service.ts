import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { DailyMealPlan } from '../../interface/daily-meal-plan';
import { FoodItemCreationRequest } from '../../interface/food-item-creation-request';
import { ConfirmationResponse } from 'src/app/interface/confirmation-response';
import { FoodItemRemovalRequest } from '../../interface/food-item-removal-request';

@Injectable({
  providedIn: 'root'
})
export class NutritionService {

  private readonly apiUrl = 'http://localhost:8222/api/nutrition';

  constructor(private http: HttpClient) {}

  public getDailyMealPlan(email: string, date: string): Observable<DailyMealPlan> {
    return this.http.get<DailyMealPlan>(`${this.apiUrl}/daily-meal-plans?email=${email}&date=${date}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public addFoodItem(request: FoodItemCreationRequest): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/food-items`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public removeFoodItem(request: FoodItemRemovalRequest): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      body: request
    };
    
    return this.http.delete<string>(`${this.apiUrl}/food-items`, httpOptions)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }
}
