import { Component } from '@angular/core';
import { NutritionService } from '../../services/nutrition/nutrition.service';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { DailyMealPlan } from '../../interface/daily-meal-plan';
import { FoodItem } from '../../interface/food-item';
import { MealRequest } from '../../interface/meal-request';

@Component({
  selector: 'app-diet',
  templateUrl: './diet.component.html',
  styleUrls: ['./diet.component.scss']
})
export class DietComponent {
  selectedDate: Date = new Date();
  formattedDate: string = '';
  dailyMealPlan: DailyMealPlan | undefined;
  totalCalories: number = 0;
  totalProtein: number = 0;
  totalFat: number = 0;
  totalCarbs: number = 0;

  constructor(private nutritionService: NutritionService) {}

  ngOnInit(): void {
    const storedDate = localStorage.getItem('selectedDate');
    this.selectedDate = storedDate ? new Date(storedDate) : new Date();
    this.displayDate();
    this.fetchDailyMealPlan();
  }

  onDateSelected(event: MatDatepickerInputEvent<Date>): void {
    this.selectedDate = event.value ?? new Date();
    this.saveSelectedDate();
    this.displayDate();
    this.fetchDailyMealPlan();
  }

  goToDate(direction: 'previous' | 'next'): void {
    const days = direction === 'next' ? 1 : -1;
    this.selectedDate.setDate(this.selectedDate.getDate() + days);
    this.saveSelectedDate();
    this.displayDate();
    this.fetchDailyMealPlan();
  }

  removeFoodItem(foodItem: FoodItem, mealName: string): void {
    const email = localStorage.getItem("userEmail");

    if(email) {
      const request: MealRequest = {
        email: email,
        date: this.formatDate(),
        mealName: mealName,
        foodItem: foodItem
      };
      
      this.nutritionService.removeFoodItem(request).subscribe(
      {
        next: (response) => {
          this.ngOnInit();
        },
        error: (error) => console.log(error)
      });
    }
  }

  private fetchDailyMealPlan(): void {
    const email = localStorage.getItem("userEmail");
  
    if(email) {
      this.nutritionService.getDailyMealPlan(email, this.formatDate()).subscribe(
      {
        next: (response) => {
          console.log(response);
          this.dailyMealPlan = response;
          this.calculateMealTotals();
          this.calculateTotals();
        },
        error: (error) => console.log(error)
      });
    }
  }

  private displayDate(): void {
    const options: Intl.DateTimeFormatOptions = { 
      weekday: 'long', 
      month: 'long', 
      day: 'numeric', 
      year: 'numeric' 
    };

    this.formattedDate = this.selectedDate.toLocaleDateString('en-US', options);
  }

  private formatDate(): string {
    const year = this.selectedDate.getFullYear();
    const month = ('0' + (this.selectedDate.getMonth() + 1)).slice(-2); // Adding 1 because months are zero-based
    const day = ('0' + this.selectedDate.getDate()).slice(-2);
  
    return `${year}-${month}-${day}`;
  }

  private saveSelectedDate(): void {
    localStorage.setItem('selectedDate', this.selectedDate.toISOString());
  }

  private calculateMealTotals(): void {
    if(this.dailyMealPlan) {
      this.dailyMealPlan.meals.forEach(meal => {
        // Calculate meal totals
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

        // Store meal totals in the meal object
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
  }
}