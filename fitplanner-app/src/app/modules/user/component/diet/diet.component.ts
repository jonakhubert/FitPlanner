import { Component } from '@angular/core';
import { NutritionService } from '../../services/nutrition/nutrition.service';
import { MealPlan } from '../../interface/meal-plan';
import { ToastrService } from 'ngx-toastr';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ChartOptions } from 'chart.js';

@Component({
  selector: 'app-diet',
  templateUrl: './diet.component.html',
  styleUrls: ['./diet.component.scss']
})
export class DietComponent {
  formattedDate: string = '';
  foodItemForm!: FormGroup;
  mealPlan: MealPlan | undefined;
  selectedMeal!: string | null;
  totalCalories: number = 0;
  totalProtein: number = 0;
  totalFat: number = 0;
  totalCarbs: number = 0;
  submitted = false;
  remainingCalories: number = 0;
  remainingProtein: number = 0;
  remainingFat: number = 0;
  remainingCarbs: number = 0;

  // pie chart
  pieChartOptions: ChartOptions<'pie'> = {
    responsive: false,
    plugins: {
      legend: {
        labels: {
          font: {
            size: 16
          },
          color: 'white'
        }
      }
    },
    datasets: {
      pie: {
        backgroundColor: ['#FF5733', '#FFC300', '#7145ff']
      }
    }
  };
  pieChartLabels = ['Protein', 'Fat', 'Carbs'];
  pieChartDatasets = [{
    data: [this.totalProtein, this.totalFat, this.totalCarbs]
  }];
  pieChartLegend = true;
  pieChartPlugins = [];

  constructor(
    private nutritionService: NutritionService,
    private formBuilder: FormBuilder,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.submitted = false;
    this.fetchMealPlan();
  }

  onDateSelected(date: string): void {
    this.formattedDate = date;
    this.fetchMealPlan();
  }

  removeFoodItem(foodId: string, mealName: string): void {
    const email = localStorage.getItem("userEmail");

    if(email) {
      this.nutritionService.removeFoodItem(email, this.formattedDate, mealName, foodId).subscribe(
      {
        next: (response) => {
          this.ngOnInit();
        },
        error: (error) => {
          console.log(error);
        }
      });
    }
  }

  addFoodItem(): void { 
    this.submitted = true;
    
    if(this.foodItemForm.invalid)
      return;

    const email = localStorage.getItem('userEmail');
    
    if(email && this.selectedMeal && this.mealPlan) {
      this.nutritionService.addFoodItem(email, this.formattedDate, this.selectedMeal, this.foodItemForm.value).subscribe(
      {
        next: (response) => {
          const closeButton = document.getElementById("closeBtn");
          closeButton?.click();
          this.ngOnInit();
          this.toastr.success(response.confirmation_message, "Success");
        },
        error: (error) => {
          console.log(error)
        }
      });
    }
  }

  openModal(meal: string) {
    this.selectedMeal = meal;

    this.foodItemForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      quantity: [1, Validators.required],
      calories: [0, Validators.required],
      protein: [0, Validators.required],
      fat: [0, Validators.required],
      carbs: [0, Validators.required]
    })
  }

  private fetchMealPlan(): void {
    const email = localStorage.getItem("userEmail");
  
    if(email && this.formattedDate) {
      this.nutritionService.getMealPlan(email, this.formattedDate).subscribe(
      {
        next: (response) => {
          console.log(response);
          this.mealPlan = response;
          this.calculateMealTotals();
          this.calculateTotals();
          this.calculateRemaining();
        },
        error: (error) => {
          console.log(error);
          if(error.status === 503)
            this.toastr.error("The Nutrition Service cannot be reached. Try again later.", "Error");
        }
      });
    }
  }

  private calculateMealTotals(): void {
    if(this.mealPlan) {
      this.mealPlan.meals.forEach(meal => {
        let totalCalories = 0;
        let totalProtein = 0;
        let totalFat = 0;
        let totalCarbs = 0;

        meal.foodItems.forEach(foodItem => {
          totalCalories += foodItem.calories;
          totalProtein += foodItem.protein;
          totalFat += foodItem.fat;
          totalCarbs += foodItem.carbs;
        });

        totalCalories = parseFloat(totalCalories.toFixed(1));
        totalProtein = parseFloat(totalProtein.toFixed(1));
        totalFat = parseFloat(totalFat.toFixed(1));
        totalCarbs = parseFloat(totalCarbs.toFixed(1));

        meal.mealTotals = {
          calories: totalCalories,
          protein: totalProtein,
          fat: totalFat,
          carbs: totalCarbs
        };
      });
    }
  }

  private calculateTotals(): void {
    this.totalCalories = 0;
    this.totalProtein = 0;
    this.totalFat = 0;
    this.totalCarbs = 0;
  
    if(this.mealPlan) {
      this.mealPlan.meals.forEach(meal => {
        this.totalCalories += meal.mealTotals.calories;
        this.totalProtein += meal.mealTotals.protein;
        this.totalFat += meal.mealTotals.fat;
        this.totalCarbs += meal.mealTotals.carbs;
      });
    }

    this.totalCalories = parseFloat(this.totalCalories.toFixed(1));
    this.totalProtein = parseFloat(this.totalProtein.toFixed(1));
    this.totalFat = parseFloat(this.totalFat.toFixed(1));
    this.totalCarbs = parseFloat(this.totalCarbs.toFixed(1));

    this.updatePieChartData();
  }

  private calculateRemaining(): void {
    if(this.mealPlan) {
      this.remainingCalories = this.mealPlan.dailyCalories - this.totalCalories;
      this.remainingProtein = this.mealPlan.dailyProtein - this.totalProtein;
      this.remainingFat = this.mealPlan.dailyFat - this.totalFat;
      this.remainingCarbs = this.mealPlan.dailyCarbs - this.totalCarbs;
    }
  }

  private updatePieChartData(): void {
    this.pieChartDatasets = [{
      data: [this.totalProtein, this.totalFat, this.totalCarbs]
    }];
  }
}