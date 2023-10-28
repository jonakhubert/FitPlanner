import { ExerciseInfo } from "./exercise-info";

export interface WorkoutPlan {
    date: string,
    exerciseInfoList: ExerciseInfo[]
}