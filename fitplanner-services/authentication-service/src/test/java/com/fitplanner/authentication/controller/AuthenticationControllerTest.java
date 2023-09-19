package com.fitplanner.authentication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitplanner.authentication.config.JwtAuthenticationFilter;
import com.fitplanner.authentication.exception.model.*;
import com.fitplanner.authentication.model.api.*;
import com.fitplanner.authentication.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerTest { // TODO: WebTestClient, WireMock, mongodb exception

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl = "/api/auth";

    @Test
    public void register_ValidRegisterRequest_VerificationMessage() throws Exception {
        // given
        var registerRequest = new RegisterRequest("any", "any", "any@gmail.com", "anyany");
        var confirmationResponse = new ConfirmationResponse("Verification email has been sent.");

        when(authenticationService.register(registerRequest)).thenReturn(confirmationResponse);

        // then
        mockMvc.perform(post(baseUrl + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value("Verification email has been sent."));
    }

    @Test
    public void register_RegisterRequestWithEmptyFirstName_ApiErrorWithStatus400() throws Exception {
        // given
        var registerRequest = new RegisterRequest("", "any", "any@gmail.com", "any");

        // then
        mockMvc.perform(post(baseUrl + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void register_RegisterRequestWithEmptyLastName_ApiErrorWithStatus400() throws Exception {
        // given
        var registerRequest = new RegisterRequest("any", "", "any@gmail.com", "any");

        // then
        mockMvc.perform(post(baseUrl + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void register_RegisterRequestWithEmptyEmail_ApiErrorWithStatus400() throws Exception {
        // given
        var registerRequest = new RegisterRequest("any", "any", "", "any");

        // then
        mockMvc.perform(post(baseUrl + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void register_RegisterRequestWithEmptyPassword_ApiErrorWithStatus400() throws Exception {
        // given
        var registerRequest = new RegisterRequest("any", "any", "any@gmail.com", "");

        // then
        mockMvc.perform(post(baseUrl + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void register_RegisterRequestWithInvalidEmailFormat_ApiErrorWithStatus400() throws Exception {
        // given
        var registerRequest = new RegisterRequest("any", "any", "invalid-format", "anyany");
        var message = registerRequest.email() + " format is invalid.";

        when(authenticationService.register(registerRequest)).thenThrow(new InvalidEmailFormatException(message));

        // then
        mockMvc.perform(post(baseUrl + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void register_RegisterRequestWithPasswordLengthLessThanSixCharacters_Status400() throws Exception {
        // given
        var registerRequest = new RegisterRequest("any", "any", "invalid-format", "any");

        // then
        mockMvc.perform(post(baseUrl + "/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void register_RegisterRequestInUnsupportedMediaType_ApiErrorWithStatus415() throws Exception {
        // given
        var registerRequest = new RegisterRequest("any", "any", "any@gmail.com", "any");
        var message = "Content-Type 'text/plain' is not supported";

        // then
        mockMvc.perform(post(baseUrl + "/register")
            .contentType(MediaType.TEXT_PLAIN_VALUE)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(jsonPath("$.statusCode").value(415))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void register_NotAllowedMethod_ApiErrorWithStatus405() throws Exception {
        // given
        var message = "Request method 'GET' is not supported";

        // then
        mockMvc.perform(get(baseUrl + "/register"))
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.statusCode").value(405))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void login_ValidLoginRequest_LoginResponseWithAccessToken() throws Exception {
        // given
        var loginRequest = new LoginRequest("any@gmail.com", "any");
        var loginResponse = new LoginResponse("token");

        when(authenticationService.login(loginRequest)).thenReturn(loginResponse);

        // then
        mockMvc.perform(post(baseUrl + "/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access_token").value("token"));
    }

    @Test
    public void login_LoginRequestWithExistingEmail_Status403() throws Exception {
        // given
        var message = "User is not verified. Verification email has been resent.";
        var loginRequest = new LoginRequest("any@gmail.com", "any");

        when(authenticationService.login(loginRequest)).thenThrow(new UserNotVerifiedException(message));

        // then
        mockMvc.perform(post(baseUrl + "/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void login_LoginRequestWithEmptyEmail_ApiErrorWithStatus400() throws Exception {
        // given
        var loginRequest = new LoginRequest("", "any");

        // then
        mockMvc.perform(post(baseUrl + "/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void login_LoginRequestWithEmptyPassword_ApiErrorWithStatus400() throws Exception {
        // given
        var loginRequest = new LoginRequest("any@gmail.com", "");

        // then
        mockMvc.perform(post(baseUrl + "/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void login_LoginRequestWithInvalidEmailFormat_ApiErrorWithStatus400() throws Exception {
        // given
        var loginRequest = new LoginRequest("invalid-format", "anyany");
        var message = loginRequest.email() + " format is invalid.";

        when(authenticationService.login(loginRequest)).thenThrow(new InvalidEmailFormatException(message));

        // then
        mockMvc.perform(post(baseUrl + "/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void login_LoginRequestInUnsupportedMediaType_ApiErrorWithStatus415() throws Exception {
        // given
        var loginRequest = new LoginRequest("any@gmail.com", "any");
        var message = "Content-Type 'text/plain' is not supported";

        // then
        mockMvc.perform(post(baseUrl + "/login")
            .contentType(MediaType.TEXT_PLAIN_VALUE)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(jsonPath("$.statusCode").value(415))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void validateAccessToken_ValidToken_Status200() throws Exception {
        // given
        var token = "token";

        when(authenticationService.isAccessTokenValid(token)).thenReturn(true);

        //then
        mockMvc.perform(post(baseUrl + "/validate-access-token")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    public void validateAccessToken_InvalidToken_Status401() throws Exception {
        // given
        var token = "token";

        when(authenticationService.isAccessTokenValid(token)).thenReturn(false);

        //then
        mockMvc.perform(post(baseUrl + "/validate-access-token")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void validateAccessToken_NoAuthorizationHeader_Status401() throws Exception {
        //then
        mockMvc.perform(post(baseUrl + "/validate-access-token")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void validateAccessToken_InvalidAuthorizationHeader_Status401() throws Exception {
        //then
        mockMvc.perform(post(baseUrl + "/validate-access-token")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Invalid"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void verify_ValidVerificationToken_ConfirmationMessage() throws Exception {
        // given
        var verificationToken = "valid-token";
        var message = "User account verified.";
        var confirmationResponse = new ConfirmationResponse(message);

        when(authenticationService.verify(verificationToken)).thenReturn(confirmationResponse);

        // then
        mockMvc.perform(get(baseUrl + "/verify")
            .param("verification_token", verificationToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value(message));
    }

    @Test
    public void verify_InvalidVerificationToken_Status404() throws Exception {
        // given
        var verificationToken = "invalid-token";
        var message = "Token not found.";

        when(authenticationService.verify(verificationToken)).thenThrow(new TokenNotFoundException(message));

        // then
        mockMvc.perform(get(baseUrl + "/verify")
            .param("verification_token", verificationToken))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void verify_NoVerificationToken_Status401() throws Exception {
        // then
        mockMvc.perform(get(baseUrl + "/verify"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void verify_ConfirmedToken_Status200() throws Exception {
        // given
        var verificationToken = "confirmed_token";
        var message = "User has been already verified.";

        when(authenticationService.verify(verificationToken)).thenThrow(new UserAlreadyVerifiedException(message));

        // then
        mockMvc.perform(get(baseUrl + "/verify")
            .param("verification_token", verificationToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void verify_ExpiredToken_Status401() throws Exception {
        // given
        var verificationToken = "expired_token";
        var message = "Token is expired.";

        when(authenticationService.verify(verificationToken)).thenThrow(new TokenExpiredException(message));

        // then
        mockMvc.perform(get(baseUrl + "/verify")
            .param("verification_token", verificationToken))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void forgotPassword_ExistingEmail_ConfirmationMessage() throws Exception {
        // given
        var email = "any@gmail.com";
        var message = "Reset password has been sent.";

        when(authenticationService.forgotPassword(email)).thenReturn(new ConfirmationResponse(message));

        //then
        mockMvc.perform(post(baseUrl + "/forgot-password")
            .param("email", email))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value(message));

    }

    @Test
    public void forgotPassword_NonExistingEmail_Status404() throws Exception {
        // given
        var email = "any@gmail.com";
        var message = "User not found.";

        when(authenticationService.forgotPassword(email)).thenThrow(new UserNotFoundException(message));

        //then
        mockMvc.perform(post(baseUrl + "/forgot-password")
            .param("email", email))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void forgotPassword_NoUserEmail_Status401() throws Exception {
        // then
        mockMvc.perform(post(baseUrl + "/forgot-password"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void resetPassword_ValidResetPasswordRequest_ConfirmationMessage() throws Exception {
        // given
        var message = "Password reset successfully.";
        var request = new ResetPasswordRequest("any@gmail.com", "token", "password");

        when(authenticationService.resetPassword(request)).thenReturn(new ConfirmationResponse(message));

        //then
        mockMvc.perform(post(baseUrl + "/reset-password")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.confirmation_message").value(message));
    }

    @Test
    public void resetPassword_ResetPasswordWithEmptyEmail_Status400() throws Exception {
        // given
        var request = new ResetPasswordRequest("", "token", "password");

        //then
        mockMvc.perform(post(baseUrl + "/reset-password")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void resetPassword_ResetPasswordWithEmptyToken_Status400() throws Exception {
        // given
        var request = new ResetPasswordRequest("any@gmail.com", "", "password");

        //then
        mockMvc.perform(post(baseUrl + "/reset-password")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void resetPassword_ResetPasswordWithEmptyPassword_Status400() throws Exception {
        // given
        var request = new ResetPasswordRequest("any@gmail.com", "token", "");

        //then
        mockMvc.perform(post(baseUrl + "/reset-password")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void resetPassword_ResetPasswordWithPasswordLessThanSixCharacters_Status400() throws Exception {
        // given
        var request = new ResetPasswordRequest("any@gmail.com", "token", "any");

        //then
        mockMvc.perform(post(baseUrl + "/reset-password")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void resetPassword_ResetPasswordRequestWithNonExistingEmail_Status404() throws Exception {
        // given
        var message = "User not found.";
        var request = new ResetPasswordRequest("any@gmail.com", "token", "password");

        when(authenticationService.resetPassword(request)).thenThrow(new UserNotFoundException(message));

        //then
        mockMvc.perform(post(baseUrl + "/reset-password")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void resetPassword_ResetPasswordRequestWithNonExistingToken_Status404() throws Exception {
        // given
        var message = "Token not found.";
        var request = new ResetPasswordRequest("any@gmail.com", "token", "password");

        when(authenticationService.resetPassword(request)).thenThrow(new TokenNotFoundException(message));

        //then
        mockMvc.perform(post(baseUrl + "/reset-password")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void resetPassword_ResetPasswordRequestWithExpiredToken_Status401() throws Exception {
        // given
        var message = "Token is expired.";
        var request = new ResetPasswordRequest("any@gmail.com", "token", "password");

        when(authenticationService.resetPassword(request)).thenThrow(new TokenExpiredException(message));

        //then
        mockMvc.perform(post(baseUrl + "/reset-password")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void validateResetPasswordToken_ValidToken_Status200() throws Exception {
        // given
        var token = "token";

        when(authenticationService.isResetPasswordTokenValid(token)).thenReturn(true);

        //then
        mockMvc.perform(post(baseUrl + "/validate-reset-password-token")
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-Reset-Password-Token", token))
            .andExpect(status().isOk());
    }

    @Test
    public void validateResetPasswordToken_InvalidToken_Status401() throws Exception {
        // given
        var token = "token";

        when(authenticationService.isResetPasswordTokenValid(token)).thenReturn(false);

        //then
        mockMvc.perform(post(baseUrl + "/validate-reset-password-token")
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-Reset-Password-Token", token))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void validateResetPasswordToken_NoToken_Status401() throws Exception {
        //then
        mockMvc.perform(post(baseUrl + "/validate-reset-password-token")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }
}
