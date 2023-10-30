import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { WorkoutPlan } from '../../interface/workout-plan';
import { StrengthExerciseRequest } from '../../interface/strength-exercise-request';
import { ConfirmationResponse } from 'src/app/interface/confirmation-response';
import { StrengthExercise } from '../../interface/strength-exercise';
import { CardioExerciseRequest } from '../../interface/cardio-exercise-request';
import { CardioExercise } from '../../interface/cardio-exercise';

@Injectable({
  providedIn: 'root'
})
export class WorkoutService {

  private readonly apiUrl = 'http://localhost:8222/api/workout';

  constructor(private http: HttpClient) {}

  public getUserWorkoutPlan(email: string, date: string): Observable<WorkoutPlan> {
    return this.http.get<WorkoutPlan>(`${this.apiUrl}/users/${email}/workout-plans/${date}`)
    .pipe(
      catchError((error: HttpErrorResponse) =>{
        throw error;
      })
    )
  }

  public addUserStrengthExercise(email: string, date: string, request: StrengthExerciseRequest): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/users/${email}/workout-plans/${date}/strength-exercises`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public removeUserStrengthExercise(email: string, date: string, id: string): Observable<ConfirmationResponse> {
    return this.http.delete<ConfirmationResponse>(`${this.apiUrl}/users/${email}/workout-plans/${date}/strength-exercises/${id}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public addUserCardioExercise(email: string, date: string, request: CardioExerciseRequest): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/users/${email}/workout-plans/${date}/cardio-exercises`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public removeUserCardioExercise(email: string, date: string, id: string): Observable<ConfirmationResponse> {
    return this.http.delete<ConfirmationResponse>(`${this.apiUrl}/users/${email}/workout-plans/${date}/cardio-exercises/${id}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public getStrengthExercises(name: string): Observable<StrengthExercise[]> {
    return this.http.get<StrengthExercise[]>(`${this.apiUrl}/strength-exercises?name=${name}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public getCardioExercises(name: string): Observable<CardioExercise[]> {
    return this.http.get<CardioExercise[]>(`${this.apiUrl}/cardio-exercises?name=${name}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }
}
