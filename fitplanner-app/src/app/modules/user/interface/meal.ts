import { FoodItem } from "./food-item";

export interface Meal {
  name: string;
  foodItems: FoodItem[];
  mealTotals: { calories: number; protein: number; fat: number; carbs: number };
}