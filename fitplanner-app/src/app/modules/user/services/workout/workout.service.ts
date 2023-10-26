import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { WorkoutPlan } from '../../interface/workout-plan';
import { ExerciseInfoRequest } from '../../interface/exercise-info-request';
import { ConfirmationResponse } from 'src/app/interface/confirmation-response';
import { Exercise } from '../../interface/exercise';

@Injectable({
  providedIn: 'root'
})
export class WorkoutService {

  private readonly apiUrl = 'http://localhost:8222/api/workout';

  constructor(private http: HttpClient) {}

  public getWorkoutPlan(email: string, date: string): Observable<WorkoutPlan> {
    return this.http.get<WorkoutPlan>(`${this.apiUrl}/users/${email}/workout-plans/${date}`)
    .pipe(
      catchError((error: HttpErrorResponse) =>{
        throw error;
      })
    )
  }

  public addExerciseInfo(email: string, date: string, request: ExerciseInfoRequest): Observable<ConfirmationResponse> {
    return this.http.post<ConfirmationResponse>(`${this.apiUrl}/users/${email}/workout-plans/${date}/exercises`, request)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public removeExerciseInfo(email: string, date: string, id: string): Observable<ConfirmationResponse> {
    return this.http.delete<ConfirmationResponse>(`${this.apiUrl}/users/${email}/workout-plans/${date}/exercises/${id}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }

  public getExercises(name: string): Observable<Exercise[]> {
    return this.http.get<Exercise[]>(`${this.apiUrl}/exercises?name=${name}`)
    .pipe(
      catchError((error: HttpErrorResponse) => {
        throw error;
      })
    )
  }
}
