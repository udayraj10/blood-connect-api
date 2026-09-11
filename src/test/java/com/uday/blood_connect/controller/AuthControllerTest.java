package com.uday.blood_connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uday.blood_connect.dto.request.LoginRequestDTO;
import com.uday.blood_connect.dto.request.RegisterDTO;
import com.uday.blood_connect.dto.response.JwtResponse;
import com.uday.blood_connect.enums.AccountType;
import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.exception.UserAlreadyExistsException;
import com.uday.blood_connect.security.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityTestBeans.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void registerUser_WhenUserNotExists_ThenReturn201() throws Exception {
        RegisterDTO registerDTO = createRegisterDTO();
        JwtResponse response = createJwtResponse("Registered Successfully.");

        when(authService.registerUser(any(RegisterDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt_token"));
    }

    @Test
    void registerUser_WhenUserExists_ThenReturn409() throws Exception {
        RegisterDTO registerDTO = createRegisterDTO();

        when(authService.registerUser(any(RegisterDTO.class))).
                thenThrow(new UserAlreadyExistsException("test@gmail.com"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("test@gmail.com"));
    }

    @Test
    void loginUser_WhenUserExists_ThenReturn200() throws Exception {
        LoginRequestDTO loginRequestDTO = createLoginRequestDTO();
        JwtResponse response = createJwtResponse("Login successfully");

        when(authService.loginUser(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt_token"));
    }

    @Test
    void loginUser_WhenUserNotExists_ThenReturn401() throws Exception {
        LoginRequestDTO loginRequestDTO = createLoginRequestDTO();

        when(authService.loginUser(any(LoginRequestDTO.class)))
                .thenThrow(new BadCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().is(401))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    private RegisterDTO createRegisterDTO() {
        return RegisterDTO.builder()
                .fullName("Uday")
                .email("test@gmail.com")
                .password("000000")
                .age(20)
                .phone("1234567890")
                .bloodGroup(BloodGroup.O_POS)
                .city("Bengaluru")
                .address("Madiwala")
                .accountType(AccountType.INDIVIDUAL)
                .isAvailable(true)
                .build();
    }

    private LoginRequestDTO createLoginRequestDTO() {
        return new LoginRequestDTO("test@gmail.com", "000000");
    }

    private JwtResponse createJwtResponse(String message) {
        return new JwtResponse(message, "jwt_token");
    }
}
