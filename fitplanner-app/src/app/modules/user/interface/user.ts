import { DailyMealPlan } from "./daily-meal-plan";
import { NutritionInfo } from "./nutrition-info";

export interface User {
    firstName: string,
    lastName: string,
    email: string,
    nutritionInfo: NutritionInfo,
    dailyMealPlans: DailyMealPlan[] 
}