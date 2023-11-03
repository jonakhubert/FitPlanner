import { ExerciseType } from "./exercise-type";

export interface Exercise {
    id: string,
    name: string,
    link: string,
    muscle: string,
    exerciseType: ExerciseType
}