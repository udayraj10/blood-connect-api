package com.uday.blood_connect.controller;

import com.uday.blood_connect.exception.CustomAccessDeniedException;
import com.uday.blood_connect.security.JwtAuthEntryPoint;
import com.uday.blood_connect.security.JwtUtil;
import com.uday.blood_connect.security.UserDetailsServiceImpl;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SecurityTestBeans {

    @Bean
    public JwtUtil jwtUtil() {
        return Mockito.mock(JwtUtil.class);
    }

    @Bean
    public JwtAuthEntryPoint jwtAuthEntryPoint() {
        return Mockito.mock(JwtAuthEntryPoint.class);
    }

    @Bean
    public CustomAccessDeniedException customAccessDeniedException() {
        return Mockito.mock(CustomAccessDeniedException.class);
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return Mockito.mock(UserDetailsServiceImpl.class);
    }
}