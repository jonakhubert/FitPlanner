import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { MealPlan } from '../../interface/meal-plan';
import { FoodItemRequest } from '../../interface/food-item-request';
import { ConfirmationResponse } from 'src/app/interface/confirmation-response';
import { Product } from '../../interface/product';

@Injectable({
  providedIn: 'root'
})
export class NutritionService {

  private readonly apiUrl = 'http://localhost:8222/api/nutrition';

  constructor(private http: HttpClient) {}

  public getMealPlan(email: string, date: string): Observable<MealPlan> {
    return this.http.get<MealPlan>(`${this.apiUrl}/users/${email}/meal-plans/${date}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public addFoodItem(email: string, date: string, mealName: string, request: FoodItemRequest): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/users/${email}/meal-plans/${date}/meals/${mealName}/food-items`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public removeFoodItem(email: string, date: string, mealName: string, id: string): Observable<ConfirmationResponse> {
    return this.http.delete<ConfirmationResponse>(`${this.apiUrl}/users/${email}/meal-plans/${date}/meals/${mealName}/food-items/${id}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public getProducts(name: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/products?name=${name}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }
}
