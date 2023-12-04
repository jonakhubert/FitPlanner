import { FoodItem } from "./food-item";

export interface Meal {
  name: string;
  foodItemList: FoodItem[];
  mealTotals: { calories: number; protein: number; fat: number; carbs: number };
}