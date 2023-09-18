package com.fitplanner.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitplanner.authentication.config.SecurityConfig;
import com.fitplanner.authentication.exception.model.*;
import com.fitplanner.authentication.model.api.LoginRequest;
import com.fitplanner.authentication.model.api.LoginResponse;
import com.fitplanner.authentication.model.api.RegisterRequest;
import com.fitplanner.authentication.repository.AccessTokenRepository;
import com.fitplanner.authentication.service.AuthenticationService;
import com.fitplanner.authentication.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfig.class)
public class AuthenticationControllerTest { // TODO: WebTestClient, WireMock, mongodb exception

    @MockBean
    private JwtService jwtService;
    @MockBean
    private AccessTokenRepository accessTokenRepository;
    @MockBean
    private AuthenticationProvider authenticationProvider;
    @MockBean
    private LogoutHandler logoutHandler;
    @MockBean
    private AuthenticationEntryPoint authenticationEntryPoint;
    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Test
//    public void register_ValidRegisterRequest_VerificationMessage() throws Exception {
//        // given
//        var registerRequest = new RegisterRequest("any", "any", "any@gmail.com", "anyany");
//        var verificationResponse = new RegisterResponse("Verification email has been sent.");
//
//        when(authenticationService.register(registerRequest)).thenReturn(verificationResponse);
//
//        // then
//        mockMvc.perform(post("/api/auth/register")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(registerRequest)))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.verification_message").value("Verification email has been sent."));
//    }

    @Test
    public void register_RegisterRequestWithEmptyFirstName_ApiErrorWithStatus400() throws Exception {
        // given
        var registerRequest = new RegisterRequest("", "any", "any@gmail.com", "any");

        // then
        mockMvc.perform(post("/api/auth/register")
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
        mockMvc.perform(post("/api/auth/register")
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
        mockMvc.perform(post("/api/auth/register")
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
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void register_RegisterRequestWithPasswordLengthLessThanSixCharacters_Status400() throws Exception {
        // given
        var registerRequest = new RegisterRequest("any", "any", "invalid-format", "any");

        // then
        mockMvc.perform(post("/api/auth/register")
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
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void register_RegisterRequestInUnsupportedMediaType_ApiErrorWithStatus415() throws Exception {
        // given
        var registerRequest = new RegisterRequest("any", "any", "any@gmail.com", "any");
        var message = "Content-Type 'text/plain;charset=UTF-8' is not supported";

        // then
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.TEXT_PLAIN_VALUE)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(jsonPath("$.statusCode").value(415))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void register_RegisterRequestWithExistingEmail_ApiErrorWithStatus409() throws Exception {
        // given
        var registerRequest = new RegisterRequest("any", "any", "any@gmail.com", "anyany");
        var message = registerRequest.email() + " already exist.";

        when(authenticationService.register(registerRequest)).thenThrow(new UserAlreadyExistException(message));

        // then
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.statusCode").value(409))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void register_NotAllowedMethod_ApiErrorWithStatus405() throws Exception {
        // given
        var message = "Request method 'GET' is not supported";

        // then
        mockMvc.perform(get("/api/auth/register"))
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.statusCode").value(405))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void login_ValidLoginRequest_LoginResponseWithAccessToken() throws Exception {
        // given
        var loginRequest = new LoginRequest("any@gmail.com", "any");
        var authenticationResponse = new LoginResponse("token");

        when(authenticationService.login(loginRequest)).thenReturn(authenticationResponse);

        // then
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access_token").value("token"));
    }

    @Test
    public void login_LoginRequestWithEmptyEmail_ApiErrorWithStatus400() throws Exception {
        // given
        var loginRequest = new LoginRequest("", "any");

        // then
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void authenticate_AuthenticateRequestWithEmptyPassword_ApiErrorWithStatus400() throws Exception {
        // given
        var loginRequest = new LoginRequest("any@gmail.com", "");

        // then
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void login_LoginRequestWithInvalidEmailFormat_ApiErrorWithStatus400() throws Exception {
        // given
        var loginRequest = new LoginRequest("invalid-format", "any");
        var message = loginRequest.email() + " format is invalid.";

        when(authenticationService.login(loginRequest))
                .thenThrow(new InvalidEmailFormatException(message));

        // then
        mockMvc.perform(post("/api/auth/login")
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
        var message = "Content-Type 'text/plain;charset=UTF-8' is not supported";

        // then
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.TEXT_PLAIN_VALUE)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(jsonPath("$.statusCode").value(415))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void login_LoginRequestWithNonExistingEmail_ApiErrorWithStatus404() throws Exception {
        // given
        var loginRequest = new LoginRequest("non-existing@gmail.com", "any");
        var message = "User not found.";

        when(authenticationService.login(loginRequest)).thenThrow(new UserNotFoundException(message));

        // then
        mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.statusCode").value(404))
            .andExpect(jsonPath("$.message").value(message));
    }

//    @Test
//    public void verify_ValidVerificationToken_VerificationMessage() throws Exception {
//        // given
//        var verificationToken = "valid-token";
//        var message = "User account verified.";
//        var registerResponse = new RegisterResponse(message);
//
//        when(authenticationService.verify(verificationToken)).thenReturn(registerResponse);
//
//        // then
//        mockMvc.perform(get("/api/auth/verify")
//            .param("verification_token", verificationToken))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.verification_message").value(message));
//    }

    @Test
    public void verify_InvalidVerificationToken_Status404() throws Exception {
        // given
        var verificationToken = "invalid-token";
        var message = "Token not found " + verificationToken;

        when(authenticationService.verify(verificationToken)).thenThrow(new TokenNotFoundException(message));

        // then
        mockMvc.perform(get("/api/auth/verify")
            .param("verification_token", verificationToken))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void verify_ConfirmedToken_Status200() throws Exception {
        // given
        var verificationToken = "confirmed_token";
        var message = "User has been already verified.";

        when(authenticationService.verify(verificationToken)).thenThrow(new UserAlreadyVerifiedException(message));

        // then
        mockMvc.perform(get("/api/auth/verify")
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
        mockMvc.perform(get("/api/auth/verify")
            .param("verification_token", verificationToken))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void verify_VerificationTokenNotProvided_Status401() throws Exception {
        // then
        mockMvc.perform(get("/api/auth/verify"))
            .andExpect(status().isUnauthorized());
    }

//    @Test
//    public void validateToken_ValidAuthorization_Status200() throws Exception {
//        // given
//        var token = "valid-token";
//
//        when(authenticationService.isTokenValid(token)).thenReturn(true);
//
//        // then
//        mockMvc.perform(post("/api/auth/validate-token")
//            .header("Authorization", "Bearer " + token))
//            .andExpect(status().isOk());
//    }

//    @Test
//    public void validateToken_InvalidAuthorization_Status401() throws Exception {
//        // then
//        mockMvc.perform(post("/api/auth/validate-token")
//            .header("Authorization", ""))
//            .andExpect(status().isUnauthorized());
//    }
}
