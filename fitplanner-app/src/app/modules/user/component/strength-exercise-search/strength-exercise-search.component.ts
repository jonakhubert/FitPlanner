import { Component } from '@angular/core';
import { StrengthExercise } from '../../interface/strength-exercise';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { WorkoutService } from '../../services/workout/workout.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { StrengthExerciseRequest } from '../../interface/strength-exercise-request';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-strength-exercise-search',
  templateUrl: './strength-exercise-search.component.html',
  styleUrls: ['./strength-exercise-search.component.scss']
})
export class StrengthExerciseSearchComponent {

  strengthExercises: StrengthExercise[] = [];
  searchQuery: string = '';
  showNoResultsMessage: boolean = false;
  selectedStrengthExercise: StrengthExercise | null = null;
  date: string = '';
  strengthExerciseForm!: FormGroup;
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

  searchStrengthExercises(name: string) {
    this.workoutService.getStrengthExercises(name).subscribe(
    {
      next: (response) => {
        console.log(response);
        if (response && response.length > 0) {
          this.strengthExercises = response;
          this.showNoResultsMessage = false;
        } else {
          this.strengthExercises = [];
          this.showNoResultsMessage = true;
        }

        this.selectedStrengthExercise = null;
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  selectStrengthExercise(exercise: StrengthExercise) {
    this.selectedStrengthExercise = exercise;

    this.strengthExerciseForm = this.formBuilder.group({
      sets: [0, [Validators.required, Validators.min(1)]],
      reps: [0, [Validators.required, Validators.min(1)]],
      weight: [0, [Validators.required, Validators.min(1)]]
    })
  }

  onSubmit() {
    this.submitted = true;

    if(this.strengthExerciseForm.invalid)
      return;

    const email = localStorage.getItem('userEmail');
    
    if(email && this.selectedStrengthExercise && this.strengthExerciseForm) {
      const request: StrengthExerciseRequest = {
        name: this.selectedStrengthExercise.name,
        link: this.selectedStrengthExercise.link,
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
    return this.sanitizer.bypassSecurityTrustResourceUrl(this.selectedStrengthExercise!.link);
  }
}
