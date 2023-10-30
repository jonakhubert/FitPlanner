import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { WorkoutService } from '../../services/workout/workout.service';
import { Exercise } from '../../interface/exercise';
import { CardioExerciseRequest } from '../../interface/cardio-exercise-request';
import { DomSanitizer } from '@angular/platform-browser';
import { ExerciseType } from '../../interface/exercise-type';
import { StrengthExerciseRequest } from '../../interface/strength-exercise-request';

@Component({
  selector: 'app-exercise-search',
  templateUrl: './exercise-search.component.html',
  styleUrls: ['./exercise-search.component.scss']
})
export class ExerciseSearchComponent {

  exercises: Exercise[] = [];
  selectedExercise: Exercise | null = null;
  searchQuery: string = '';
  showNoResultsMessage: boolean = false;
  date: string = '';
  type: string = '';
  submitted: boolean = false;
  cardioExerciseForm!: FormGroup;
  strengthExerciseForm!: FormGroup;

  constructor(
    private workoutService: WorkoutService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private sanitizer: DomSanitizer
  ) {}
  
  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.date = params['date'];
      this.type = params['type'];
    })
  }

  searchExercises(name: string) {
    this.workoutService.getExercises(name, this.type).subscribe(
    {
      next: (response) => {
        console.log(response);
        if (response && response.length > 0) {
          this.exercises = response;
          this.showNoResultsMessage = false;
        } else {
          this.exercises = [];
          this.showNoResultsMessage = true;
        }

        this.selectedExercise = null;
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  selectExercise(exercise: Exercise) {
    this.selectedExercise = exercise;

    if(this.selectedExercise.exerciseType === ExerciseType.CARDIO) {
      this.cardioExerciseForm = this.formBuilder.group({
        minutes: [0, [Validators.required, Validators.min(1)]]
      })
    } else if(this.selectedExercise.exerciseType === ExerciseType.STRENGTH) {
      this.strengthExerciseForm = this.formBuilder.group({
        sets: [0, [Validators.required, Validators.min(1)]],
        reps: [0, [Validators.required, Validators.min(1)]],
        weight: [0, [Validators.required, Validators.min(1)]]
      })
    }
  }

  onCardioSubmit() {
    this.submitted = true;

    if(this.cardioExerciseForm.invalid)
      return;

    const email = localStorage.getItem('userEmail');
    
    if(email && this.selectedExercise && this.cardioExerciseForm) {
      const request: CardioExerciseRequest = {
        name: this.selectedExercise.name,
        minutes: this.cardioExerciseForm.get('minutes')!.value
      }
      
      this.workoutService.addUserCardioExercise(email, this.date, request).subscribe(
      {
        next: (response) => {
          this.router.navigate(['/user/workout']);
          this.toastr.success(response.confirmation_message, "Success");
        },
        error: (error) => {
          console.log(error)
        }
      });
    }
  }

  onStrengthSubmit() {
    this.submitted = true;

    if(this.strengthExerciseForm.invalid)
      return;

    const email = localStorage.getItem('userEmail');
    
    if(email && this.selectedExercise && this.strengthExerciseForm) {
      const request: StrengthExerciseRequest = {
        name: this.selectedExercise.name,
        link: this.selectedExercise.link,
        sets: this.strengthExerciseForm.get('sets')!.value,
        reps: this.strengthExerciseForm.get('reps')!.value,
        weight: this.strengthExerciseForm.get('weight')!.value
      }
      
      this.workoutService.addUserStrengthExercise(email, this.date, request).subscribe(
      {
        next: (response) => {
          this.router.navigate(['/user/workout']);
          this.toastr.success(response.confirmation_message, "Success");
        },
        error: (error) => {
          console.log(error)
        }
      });
    }
  }

  getSafeUrl() {
    return this.sanitizer.bypassSecurityTrustResourceUrl(this.selectedExercise!.link);
  }
}
