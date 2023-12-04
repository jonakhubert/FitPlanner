import { UserCardioExercise } from "./user-cardio-exercise";
import { UserStrengthExercise } from "./user-strength-exercise";

export interface WorkoutPlan {
    date: string,
    strengthExerciseList: UserStrengthExercise[],
    cardioExerciseList: UserCardioExercise[]
}