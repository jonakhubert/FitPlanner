package com.fitplanner.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitplanner.authentication.config.JwtAuthenticationFilter;
import com.fitplanner.authentication.config.SecurityConfig;
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
    public void shouldReturnAuthenticationResponseWithTokenWhenValidRegisterRequestIsProvided() throws Exception {
        // given
        RegisterRequest registerRequest = new RegisterRequest("any", "any", "any@gmail.com", "any");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("token");

        when(authenticationService.register(registerRequest)).thenReturn(authenticationResponse);

        // when & then
        mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access_token").value("token"));
    }
}
