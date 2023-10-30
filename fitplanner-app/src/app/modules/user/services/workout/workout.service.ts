import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { WorkoutPlan } from '../../interface/workout-plan';
import { ConfirmationResponse } from 'src/app/interface/confirmation-response';
import { ExerciseType } from '../../interface/exercise-type';
import { Exercise } from '../../interface/exercise';
import { CardioExerciseRequest } from '../../interface/cardio-exercise-request';
import { StrengthExerciseRequest } from '../../interface/strength-exercise-request';


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

  public getExercises(name: string, type: string): Observable<Exercise[]> {
    return this.http.get<Exercise[]>(`${this.apiUrl}/exercises?name=${name}&type=${type}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }
}
