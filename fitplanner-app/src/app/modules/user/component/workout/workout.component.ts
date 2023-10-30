import { Component } from '@angular/core';
import { WorkoutService } from '../../services/workout/workout.service';
import { WorkoutPlan } from '../../interface/workout-plan';
import { UserStrengthExercise } from '../../interface/user-strength-exercise';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-workout',
  templateUrl: './workout.component.html',
  styleUrls: ['./workout.component.scss']
})
export class WorkoutComponent {
  formattedDate: string = '';
  workoutPlan: WorkoutPlan | undefined;
  selectedUserStrengthExercise: UserStrengthExercise | undefined;

  constructor(
    private workoutService: WorkoutService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.fetchWorkoutPlan();
  }

  onDateSelected(date: string): void {
    this.formattedDate = date;
    this.fetchWorkoutPlan();
  }

  removeUserStrengthExercise(id: string): void {
    const email = localStorage.getItem('userEmail');

    if(email) {
      this.workoutService.removeUserStrengthExercise(email, this.formattedDate, id).subscribe(
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

  removeUserCardioExercise(id: string): void {
    const email = localStorage.getItem('userEmail');

    if(email) {
      this.workoutService.removeUserCardioExercise(email, this.formattedDate, id).subscribe(
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

  openModal(exercise: UserStrengthExercise): void {
    this.selectedUserStrengthExercise = exercise;
  }

  getSafeUrl() {
    return this.sanitizer.bypassSecurityTrustResourceUrl(this.selectedUserStrengthExercise!.link);
  }

  private fetchWorkoutPlan(): void {
    const email = localStorage.getItem("userEmail");

    if(email && this.formattedDate) {
      this.workoutService.getUserWorkoutPlan(email, this.formattedDate).subscribe(
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
