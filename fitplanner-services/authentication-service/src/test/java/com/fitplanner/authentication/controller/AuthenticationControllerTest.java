package com.fitplanner.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitplanner.authentication.config.JwtAuthenticationFilter;
import com.fitplanner.authentication.config.SecurityConfig;
import com.fitplanner.authentication.exception.model.InvalidEmailFormatException;
import com.fitplanner.authentication.exception.model.UserAlreadyExistException;
import com.fitplanner.authentication.exception.model.UserNotFoundException;
import com.fitplanner.authentication.model.api.AuthenticationRequest;
import com.fitplanner.authentication.model.api.AuthenticationResponse;
import com.fitplanner.authentication.model.api.RegisterRequest;
import com.fitplanner.authentication.repository.TokenRepository;
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
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private LogoutHandler logoutHandler;

    @MockBean
    private AuthenticationEntryPoint authenticationEntryPoint;

    @MockBean
    private AuthenticationService authenticationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void register_ValidRegisterRequest_AuthenticationResponseWithAccessToken() throws Exception {
        // given
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "any@gmail.com", "any");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("token");

        when(authenticationService.register(registerRequest)).thenReturn(authenticationResponse);

        // then
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access_token").value("token"));
    }

    @Test
    public void register_RegisterRequestWithEmptyFirstName_ApiErrorWithStatus400() throws Exception {
        // given
        RegisterRequest registerRequest = new RegisterRequest("", "any", "any@gmail.com", "any");

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
        RegisterRequest registerRequest = new RegisterRequest("any", "", "any@gmail.com", "any");

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
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "", "any");

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
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "any@gmail.com", "");

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
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "invalid-format", "any");
        String message = registerRequest.email() + " format is invalid.";

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
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "any@gmail.com", "any");
        String message = "Content-Type 'text/plain;charset=UTF-8' is not supported";

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
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "any@gmail.com", "any");
        String message = registerRequest.email() + " already exist.";

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
        String message = "Request method 'GET' is not supported";

        // then
        mockMvc.perform(get("/api/auth/register"))
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.statusCode").value(405))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void authenticate_ValidAuthenticateRequest_AuthenticationResponseWithAccessToken() throws Exception {
        // given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("any@gmail.com", "any");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("token");

        when(authenticationService.authenticate(authenticationRequest)).thenReturn(authenticationResponse);

        // then
        mockMvc.perform(post("/api/auth/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authenticationRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access_token").value("token"));
    }

    @Test
    public void authenticate_AuthenticateRequestWithEmptyEmail_ApiErrorWithStatus400() throws Exception {
        // given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("", "any");

        // then
        mockMvc.perform(post("/api/auth/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authenticationRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void authenticate_AuthenticateRequestWithEmptyPassword_ApiErrorWithStatus400() throws Exception {
        // given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("any@gmail.com", "");

        // then
        mockMvc.perform(post("/api/auth/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authenticationRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void authenticate_AuthenticateRequestWithInvalidEmailFormat_ApiErrorWithStatus400() throws Exception {
        // given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("invalid-format", "any");
        String message = authenticationRequest.email() + " format is invalid.";

        when(authenticationService.authenticate(authenticationRequest))
                .thenThrow(new InvalidEmailFormatException(message));

        // then
        mockMvc.perform(post("/api/auth/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authenticationRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.statusCode").value(400))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void authenticate_AuthenticationRequestInUnsupportedMediaType_ApiErrorWithStatus415() throws Exception {
        // given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("any@gmail.com", "any");
        String message = "Content-Type 'text/plain;charset=UTF-8' is not supported";

        // then
        mockMvc.perform(post("/api/auth/authenticate")
            .contentType(MediaType.TEXT_PLAIN_VALUE)
            .content(objectMapper.writeValueAsString(authenticationRequest)))
            .andExpect(status().isUnsupportedMediaType())
            .andExpect(jsonPath("$.statusCode").value(415))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void authenticate_AuthenticationRequestWithNonExistingEmail_ApiErrorWithStatus404() throws Exception {
        // given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("non-existing@gmail.com", "any");
        String message = "User not found.";

        when(authenticationService.authenticate(authenticationRequest)).thenThrow(new UserNotFoundException(message));

        // then
        mockMvc.perform(post("/api/auth/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authenticationRequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.statusCode").value(404))
            .andExpect(jsonPath("$.message").value(message));
    }

    @Test
    public void validateToken_ValidAuthorization_Status200() throws Exception {
        // given
        String token = "valid-token";
        
        when(authenticationService.isTokenValid(token)).thenReturn(true);

        // then
        mockMvc.perform(post("/api/auth/validate-token")
            .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    public void validateToken_InvalidAuthorization_Status401() throws Exception {
        // then
        mockMvc.perform(post("/api/auth/validate-token")
            .header("Authorization", ""))
            .andExpect(status().isUnauthorized());
    }
}
