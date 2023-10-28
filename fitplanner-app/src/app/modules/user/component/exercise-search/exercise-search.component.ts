import { Component } from '@angular/core';
import { Exercise } from '../../interface/exercise';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { WorkoutService } from '../../services/workout/workout.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ExerciseInfoRequest } from '../../interface/exercise-info-request';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-exercise-search',
  templateUrl: './exercise-search.component.html',
  styleUrls: ['./exercise-search.component.scss']
})
export class ExerciseSearchComponent {

  exercises: Exercise[] = [];
  searchQuery: string = '';
  showNoResultsMessage: boolean = false;
  selectedExercise: Exercise | null = null;
  date: string = '';
  exerciseInfoForm!: FormGroup;
  submitted: boolean = false;

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

  searchExercises(name: string) {
    this.workoutService.getExercises(name).subscribe(
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

    this.exerciseInfoForm = this.formBuilder.group({
      sets: [0, [Validators.required, Validators.min(1)]],
      reps: [0, [Validators.required, Validators.min(1)]],
      weight: [0, [Validators.required, Validators.min(1)]]
    })
  }

  onSubmit() {
    this.submitted = true;

    if(this.exerciseInfoForm.invalid)
      return;

    const email = localStorage.getItem('userEmail');
    
    if(email && this.selectedExercise && this.exerciseInfoForm) {
      const request: ExerciseInfoRequest = {
        name: this.selectedExercise.name,
        link: this.selectedExercise.link,
        sets: this.exerciseInfoForm.get('sets')!.value,
        reps: this.exerciseInfoForm.get('reps')!.value,
        weight: this.exerciseInfoForm.get('weight')!.value
      }
      
      this.workoutService.addExerciseInfo(email, this.date, request).subscribe(
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
