export interface ApiError {
    path: string;
    message: string;
    statusCode: number;
    time: string;
}