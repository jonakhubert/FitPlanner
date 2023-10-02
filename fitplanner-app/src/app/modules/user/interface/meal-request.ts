import { FoodItem } from "./food-item";

export interface MealRequest {
    email: string,
    date: string,
    mealName: string,
    foodItem: FoodItem
}