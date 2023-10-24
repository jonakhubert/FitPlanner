import { Meal } from "./meal";

export interface MealPlan {
    date: string,
    meals: Meal[],
    dailyCalories: number,
    dailyProtein: number,
    dailyFat: number,
    dailyCarbs: number
}