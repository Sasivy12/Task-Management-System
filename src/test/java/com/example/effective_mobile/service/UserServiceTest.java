package com.example.effective_mobile.service;

import com.example.effective_mobile.exception.AuthenticationFailedException;
import com.example.effective_mobile.exception.UserAlreadyExistsException;
import com.example.effective_mobile.model.User;
import com.example.effective_mobile.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private Authentication authentication;

    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encoder = new BCryptPasswordEncoder(12);
    }

    @Test
    void testRegister_UserAlreadyExists()
    {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.register(user));

        // Verify no save operation
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void testRegister_NewUser() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        // Act
        userService.register(user);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
        assertTrue(encoder.matches("password", user.getPassword()));
    }

    @Test
    void testVerify_SuccessfulAuthentication() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(user.getEmail())).thenReturn("mock-jwt-token");

        // Act
        String result = userService.verify(user);

        // Assert
        assertEquals("mock-jwt-token", result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(user.getEmail());
    }

    @Test
    void testVerify_FailedAuthentication() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("wrong-password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        AuthenticationFailedException exception = assertThrows(AuthenticationFailedException.class, () -> {
            userService.verify(user);
        });

        assertEquals("Authentication failed for user: test@example.com", exception.getMessage());
        verify(jwtService, never()).generateToken(anyString());
    }


}
