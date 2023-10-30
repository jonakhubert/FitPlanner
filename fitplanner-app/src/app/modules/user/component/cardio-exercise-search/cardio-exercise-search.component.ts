import { Component } from '@angular/core';
import { CardioExercise } from '../../interface/cardio-exercise';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CardioExerciseRequest } from '../../interface/cardio-exercise-request';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { WorkoutService } from '../../services/workout/workout.service';

@Component({
  selector: 'app-cardio-exercise-search',
  templateUrl: './cardio-exercise-search.component.html',
  styleUrls: ['./cardio-exercise-search.component.scss']
})
export class CardioExerciseSearchComponent {

  cardioExercises: CardioExercise[] = [];
  searchQuery: string = '';
  showNoResultsMessage: boolean = false;
  selectedCardioExercise: CardioExercise | null = null;
  date: string = '';
  cardioExerciseForm!: FormGroup;
  submitted: boolean = false;

  constructor(
    private workoutService: WorkoutService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.date = params['date'];
    })
  }

  searchCardioExercises(name: string) {
    this.workoutService.getCardioExercises(name).subscribe(
    {
      next: (response) => {
        console.log(response);
        if (response && response.length > 0) {
          this.cardioExercises = response;
          this.showNoResultsMessage = false;
        } else {
          this.cardioExercises = [];
          this.showNoResultsMessage = true;
        }

        this.selectedCardioExercise = null;
      },
      error: (error) => {
        console.log(error);
      }
    })
  }

  selectCardioExercise(exercise: CardioExercise) {
    this.selectedCardioExercise = exercise;

    this.cardioExerciseForm = this.formBuilder.group({
      minutes: [0, [Validators.required, Validators.min(1)]]
    })
  }

  onSubmit() {
    this.submitted = true;

    if(this.cardioExerciseForm.invalid)
      return;

    const email = localStorage.getItem('userEmail');
    
    if(email && this.selectedCardioExercise && this.cardioExerciseForm) {
      const request: CardioExerciseRequest = {
        name: this.selectedCardioExercise.name,
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
}
