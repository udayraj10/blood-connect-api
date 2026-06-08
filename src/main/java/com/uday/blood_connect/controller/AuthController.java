package com.uday.blood_connect.controller;

import com.uday.blood_connect.dto.response.JwtResponse;
import com.uday.blood_connect.dto.request.LoginRequestDTO;
import com.uday.blood_connect.dto.request.RegisterDTO;
import com.uday.blood_connect.security.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication Controller", description = "Endpoints for user registration and login")
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> registerUser(@Valid @RequestBody RegisterDTO registerRequest) {
        return ResponseEntity.ok(authService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.loginUser(loginRequest));
    }
}
