import { Component } from '@angular/core';
import { NutritionService } from '../../services/nutrition/nutrition.service';
import { DailyMealPlan } from '../../interface/daily-meal-plan';
import { FoodItemCreationRequest } from '../../interface/food-item-creation-request';
import { ToastrService } from 'ngx-toastr';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FoodItemRemovalRequest } from '../../interface/food-item-removal-request';
import { ChartOptions } from 'chart.js';

@Component({
  selector: 'app-diet',
  templateUrl: './diet.component.html',
  styleUrls: ['./diet.component.scss']
})
export class DietComponent {
  formattedDate: string = '';
  foodItemForm!: FormGroup;
  dailyMealPlan: DailyMealPlan | undefined;
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
    this.fetchDailyMealPlan();
  }

  onDateSelected(date: string): void {
    this.formattedDate = date;
    this.fetchDailyMealPlan();
  }

  removeFoodItem(foodId: string, mealName: string): void {
    const email = localStorage.getItem("userEmail");

    if(email) {
      const request: FoodItemRemovalRequest = {
        email: email,
        date: this.formattedDate,
        mealName: mealName,
        foodId: foodId
      };
      
      this.nutritionService.removeFoodItem(request).subscribe(
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

  addFoodItem() { 
    this.submitted = true;
    
    if(this.foodItemForm.invalid)
      return;

    const email = localStorage.getItem('userEmail');
    if(email && this.selectedMeal && this.dailyMealPlan) {
      const request: FoodItemCreationRequest = {
        email: email,
        date: this.formattedDate,
        mealName: this.selectedMeal,
        foodItem: this.foodItemForm.value
      }
      
      this.nutritionService.addFoodItem(request).subscribe(
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

  private fetchDailyMealPlan(): void {
    const email = localStorage.getItem("userEmail");
  
    if(email && this.formattedDate) {
      this.nutritionService.getDailyMealPlan(email, this.formattedDate).subscribe(
      {
        next: (response) => {
          console.log(response);
          this.dailyMealPlan = response;
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
    if(this.dailyMealPlan) {
      this.dailyMealPlan.meals.forEach(meal => {
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
  
    if(this.dailyMealPlan) {
      this.dailyMealPlan.meals.forEach(meal => {
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
    if(this.dailyMealPlan) {
      this.remainingCalories = this.dailyMealPlan.dailyCalories - this.totalCalories;
      this.remainingProtein = this.dailyMealPlan.dailyProtein - this.totalProtein;
      this.remainingFat = this.dailyMealPlan.dailyFat - this.totalFat;
      this.remainingCarbs = this.dailyMealPlan.dailyCarbs - this.totalCarbs;
    }
  }

  private updatePieChartData(): void {
    this.pieChartDatasets = [{
      data: [this.totalProtein, this.totalFat, this.totalCarbs]
    }];
  }
}