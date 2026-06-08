package com.uday.blood_connect.security;

import com.uday.blood_connect.dto.response.JwtResponse;
import com.uday.blood_connect.dto.request.LoginRequestDTO;
import com.uday.blood_connect.dto.request.RegisterDTO;
import com.uday.blood_connect.entity.User;
import com.uday.blood_connect.enums.Role;
import com.uday.blood_connect.exception.RoleNotFoundException;
import com.uday.blood_connect.exception.UserAlreadyExistsException;
import com.uday.blood_connect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public JwtResponse registerUser(RegisterDTO registerDTO) {

        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + registerDTO.getEmail());
        }

        registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        User user = User.builder()
                .fullName(registerDTO.getFullName())
                .email(registerDTO.getEmail())
                .password(registerDTO.getPassword())
                .age(registerDTO.getAge())
                .phone(registerDTO.getPhone())
                .bloodGroup(registerDTO.getBloodGroup())
                .city(registerDTO.getCity())
                .address(registerDTO.getAddress())
                .accountType(registerDTO.getAccountType())
                .role(Role.USER) // Default role is USER
                .isAvailable(registerDTO.getIsAvailable())
                .lastDonationDate(registerDTO.getLastDonationDate())
                .isActive(true)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new JwtResponse("User registered successfully", token);
    }

    public JwtResponse loginUser(LoginRequestDTO loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );

            UserDetails principal = (UserDetails) authentication.getPrincipal();

            String role = principal.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElseThrow(() -> new RoleNotFoundException("User has no role assigned"));

            String token = jwtUtil.generateToken(loginRequest.email(), role);

            return new JwtResponse("Login successfully", token);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
