import { Meal } from "./meal";

export interface MealPlan {
    date: string,
    mealList: Meal[],
    dailyCalories: number,
    dailyProtein: number,
    dailyFat: number,
    dailyCarbs: number
}