export interface ResetPasswordRequest {
    email: string,
    resetPasswordToken: string,
    newPassword: string
}