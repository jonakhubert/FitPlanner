import { Component } from '@angular/core';

@Component({
  selector: 'app-workout',
  templateUrl: './workout.component.html',
  styleUrls: ['./workout.component.scss']
})
export class WorkoutComponent {
  formattedDate: string = '';

  onDateSelected(date: string): void {
    this.formattedDate = date;
    console.log(date);
  }
}
