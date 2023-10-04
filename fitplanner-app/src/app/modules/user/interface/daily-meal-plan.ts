import { Meal } from "./meal";

export interface DailyMealPlan {
    date: string,
    meals: Meal[]
}