import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { StrengthExerciseRequest } from '../../interface/strength-exercise-request';
import { WorkoutService } from '../../services/workout/workout.service';
import { Exercise } from '../../interface/exercise';

@Component({
  selector: 'app-muscle-search',
  templateUrl: './muscle-search.component.html',
  styleUrls: ['./muscle-search.component.scss']
})
export class MuscleSearchComponent {

  exercises: Exercise[] = [];
  selectedExercise: Exercise | null = null;
  searchQuery: string = '';
  showNoResultsMessage: boolean = false;
  date: string = '';
  submitted: boolean = false;
  exerciseForm!: FormGroup;

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
    })
  }

  searchExercises(muscle: string) {
    this.workoutService.getExercisesByMuscle(muscle).subscribe(
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

    this.exerciseForm = this.formBuilder.group({
      sets: [0, [Validators.required, Validators.min(1)]],
      reps: [0, [Validators.required, Validators.min(1)]],
      weight: [0, [Validators.required, Validators.min(1)]]
    })
  }

  onSubmit() {
    this.submitted = true;

    if(this.exerciseForm.invalid)
      return;

    const email = localStorage.getItem('userEmail');
    
    if(email && this.selectedExercise && this.exerciseForm) {
      const request: StrengthExerciseRequest = {
        name: this.selectedExercise.name,
        link: this.selectedExercise.link,
        sets: this.exerciseForm.get('sets')!.value,
        reps: this.exerciseForm.get('reps')!.value,
        weight: this.exerciseForm.get('weight')!.value
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