import { UserStrengthExercise } from "./user-strength-exercise";

export interface WorkoutPlan {
    date: string,
    strengthExerciseList: UserStrengthExercise[]
}