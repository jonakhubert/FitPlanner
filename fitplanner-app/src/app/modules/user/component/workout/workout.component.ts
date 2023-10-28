import { Component } from '@angular/core';
import { WorkoutService } from '../../services/workout/workout.service';
import { WorkoutPlan } from '../../interface/workout-plan';
import { ExerciseInfo } from '../../interface/exercise-info';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-workout',
  templateUrl: './workout.component.html',
  styleUrls: ['./workout.component.scss']
})
export class WorkoutComponent {
  formattedDate: string = '';
  workoutPlan: WorkoutPlan | undefined;
  selectedExerciseInfo: ExerciseInfo | undefined;

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

  openModal(exerciseInfo: ExerciseInfo): void {
    this.selectedExerciseInfo = exerciseInfo;
  }

  getSafeUrl() {
    return this.sanitizer.bypassSecurityTrustResourceUrl(this.selectedExerciseInfo!.link);
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
