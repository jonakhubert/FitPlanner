import { FoodItem } from "./food-item";

export interface FoodItemCreationRequest {
    email: string,
    date: string,
    mealName: string,
    foodItem: FoodItem,
    calories: number,
    protein: number,
    fat: number,
    carbs: number
}