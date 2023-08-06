package com.group1.taskmanagement.controller;

import com.group1.taskmanagement.model.Role;
import com.group1.taskmanagement.model.RoleName;
import com.group1.taskmanagement.dto.AuthDto;
import com.group1.taskmanagement.dto.SignInDto;
import com.group1.taskmanagement.dto.SignUpDto;
import com.group1.taskmanagement.repository.RoleRepository;
import com.group1.taskmanagement.repository.UserRepository;
import com.group1.taskmanagement.security.JWTGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTGenerator tokenGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, userRepository, roleRepository, passwordEncoder, tokenGenerator);
    }

    @Test
    void signup() {
        Role userRole = Role.builder()
                .roleId(1L)
                .name(RoleName.USER)
                .build();

        SignUpDto signUpDto = SignUpDto.builder()
                .username("testUser")
                .firstname("John")
                .lastname("Doe")
                .password("Password123!")
                .email("john.doe@domain.com")
                .build();

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(roleRepository.findByName(any())).thenReturn(Optional.of(userRole));

        // Act
        ResponseEntity<String> response = authController.signup(signUpDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered!", response.getBody());
    }

    @Test
    void signin() {
        // Arrange
        SignInDto signInDto = SignInDto.builder()
                .username("testUser")
                .password("Password123!")
                .build();

        Authentication mockAuthentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);
        when(tokenGenerator.generateToken(mockAuthentication)).thenReturn("token");

        // Act
        ResponseEntity<AuthDto> response = authController.signin(signInDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody().getAccessToken());
    }

}
