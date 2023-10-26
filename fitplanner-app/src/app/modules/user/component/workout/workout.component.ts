import { Component } from '@angular/core';
import { WorkoutService } from '../../services/workout/workout.service';
import { FormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { WorkoutPlan } from '../../interface/workout-plan';

@Component({
  selector: 'app-workout',
  templateUrl: './workout.component.html',
  styleUrls: ['./workout.component.scss']
})
export class WorkoutComponent {
  formattedDate: string = '';
  workoutPlan: WorkoutPlan | undefined;

  constructor(
    private workoutService: WorkoutService,
    private formBuilder: FormBuilder,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.fetchWorkoutPlan();
  }

  onDateSelected(date: string): void {
    this.formattedDate = date;
    this.fetchWorkoutPlan();
  }

  removeExerciseInfo(id: string): void {
    const email = localStorage.getItem('userEmail');

    if(email) {
      this.workoutService.removeExerciseInfo(email, this.formattedDate, id).subscribe(
      {
        next: (response) => {
          this.ngOnInit();
        },
        error: (error) => {
          console.log(error);
        }
      })
    }
  }

  private fetchWorkoutPlan(): void {
    const email = localStorage.getItem("userEmail");

    if(email && this.formattedDate) {
      this.workoutService.getWorkoutPlan(email, this.formattedDate).subscribe(
      {
        next: (response) => {
          console.log(response);
          this.workoutPlan = response;
        },
        error: (error) => {
          console.log(error);
        }
      })
    }
  }
}
