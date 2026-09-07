package com.uday.blood_connect.security;

import com.uday.blood_connect.dto.request.LoginRequestDTO;
import com.uday.blood_connect.dto.request.RegisterDTO;
import com.uday.blood_connect.dto.response.JwtResponse;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.AccountType;
import com.uday.blood_connect.enums.BloodGroup;
import com.uday.blood_connect.exception.RoleNotFoundException;
import com.uday.blood_connect.exception.UserAlreadyExistsException;
import com.uday.blood_connect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerUser_WhenUserNotExists_ReturnJwtResponse () {
        RegisterDTO registerDTO = createRegisterDTO();

        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        JwtResponse response = authService.registerUser(registerDTO);

        assertNotNull(response);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_WhenUserExists_ThrowException() {
        RegisterDTO registerDTO = createRegisterDTO();

        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () ->
            authService.registerUser(registerDTO));
    }

    @Test
    void loginUser_WhenUserExists_ReturnJwtResponse() {
        LoginRequestDTO loginRequestDTO = createLoginRequestDTO();

        Authentication auth = mock(Authentication.class);
        UserDetails principal = new org.springframework.security.core.userdetails.User(
                "test@gmail.com",
                "000000",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(principal);
        when(jwtUtil.generateToken("test@gmail.com", "ROLE_USER")).thenReturn("jwt-token");

        JwtResponse response = authService.loginUser(loginRequestDTO);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());


        verify(authenticationManager, times(1)).authenticate(
                any(UsernamePasswordAuthenticationToken.class));
        verify(auth, times(1)).getPrincipal();
        verify(jwtUtil, times(1)).generateToken("test@gmail.com", "ROLE_USER");
    }

    @Test
    void loginUser_WhenBadCredentials_ThrowsException () {
        LoginRequestDTO loginRequestDTO = createLoginRequestDTO();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(
                new BadCredentialsException("Invalid email or password"));

        assertThrows(BadCredentialsException.class, () -> authService.loginUser(loginRequestDTO));


        verify(authenticationManager, times(1)).authenticate(
                any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void loginUser_WhenUserHasNoRoles_ThrowException() {
        LoginRequestDTO loginRequestDTO = createLoginRequestDTO();

        Authentication auth = mock(Authentication.class);
        UserDetails principalWithNoRoles = new org.springframework.security.core.userdetails.User(
                "test@gmail.com",
                "000000",
                Collections.emptyList()
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(principalWithNoRoles);

        assertThrows(RoleNotFoundException.class, () -> authService.loginUser(loginRequestDTO));

        verify(authenticationManager, times(1)).authenticate(
                any(UsernamePasswordAuthenticationToken.class));
        verify(auth, times(1)).getPrincipal();
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

}
