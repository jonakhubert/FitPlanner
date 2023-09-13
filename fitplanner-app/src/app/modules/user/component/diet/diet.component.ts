import { Component } from '@angular/core';
import { NutritionService } from '../../services/nutrition.service';

@Component({
  selector: 'app-diet',
  templateUrl: './diet.component.html',
  styleUrls: ['./diet.component.scss']
})
export class DietComponent {
  helloMessage: string | undefined;

  constructor(private nutritionService: NutritionService) {}

  ngOnInit() {
    this.nutritionService.hello().subscribe(
    {
      next: response => {
        this.helloMessage = response.message;
      },
      error: (error) => {
        console.log(error);
      }
    },
    );
  }
}
