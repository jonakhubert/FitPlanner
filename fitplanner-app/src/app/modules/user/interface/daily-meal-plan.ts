import { Meal } from "./meal";

export interface DailyMealPlan {
    date: string,
    meals: Meal[],
    dailyCalories: number,
    dailyProtein: number,
    dailyFat: number,
    dailyCarbs: number
}