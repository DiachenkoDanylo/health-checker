package com.diachenko.checker.unit.service;


/*  health-checker
    20.02.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.TestDataFactory;
import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.model.entity.Authority;
import com.diachenko.checker.model.payload.RegisterUserPayload;
import com.diachenko.checker.repository.AuthorityRepository;
import com.diachenko.checker.repository.UserRepository;
import com.diachenko.checker.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthorityRepository authorityRepository;

    private final RegisterUserPayload payload = TestDataFactory.userPayload();
    private final Authority userAuthority = TestDataFactory.userAuthority();
    private final AppUser validAppUser = TestDataFactory.user();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_success() {
        when(userRepository.findByUsername(payload.getUsername())).thenReturn(Optional.empty());
        when(authorityRepository.findByAuthority(anyString())).thenReturn(Optional.of(userAuthority));

        when(passwordEncoder.encode(payload.getPassword())).thenReturn("pass");
        when(userRepository.save(any())).thenReturn(validAppUser);

        assertEquals(validAppUser, authenticationService.registerUser(payload));
        verify(userRepository, times(1)).findByUsername(payload.getUsername());
    }

    @Test
    void registerUser_success_noAuthorities() {
        when(userRepository.findByUsername(payload.getUsername())).thenReturn(Optional.empty());
        when(authorityRepository.findByAuthority("USER")).thenReturn(Optional.of(userAuthority));

        when(passwordEncoder.encode(payload.getPassword())).thenReturn("pass");
        when(userRepository.save(any())).thenReturn(validAppUser);

        assertEquals(validAppUser, authenticationService.registerUser(payload));
        verify(userRepository, times(1)).findByUsername(payload.getUsername());
    }

    @Test
    void registerUser_exception_notFound() {
        when(userRepository.findByUsername(payload.getUsername())).thenReturn(Optional.empty());
        when(authorityRepository.findByAuthority(anyString())).thenReturn(Optional.of(userAuthority));

        when(passwordEncoder.encode(payload.getPassword())).thenReturn("pass");
        when(userRepository.save(any())).thenReturn(validAppUser);

        assertEquals(validAppUser, authenticationService.registerUser(payload));
        verify(userRepository, times(1)).findByUsername(payload.getUsername());
    }

    @Test
    void fetchAuthorities_success() {
        when(authorityRepository.findByAuthority("USER")).thenReturn(Optional.of(userAuthority));

        assertEquals(validAppUser.getAuthorities(), authenticationService.fetchAuthorities(Set.of("USER")));
        verify(authorityRepository, times(1)).findByAuthority("USER");
    }

    @Test
    void fetchAuthorities_success_empty() {
        when(authorityRepository.findByAuthority("ADMIN")).thenReturn(Optional.empty());
        when(authorityRepository.findByAuthority("USER")).thenReturn(Optional.of(userAuthority));

        assertEquals(validAppUser.getAuthorities(), authenticationService.fetchAuthorities(Set.of("ADMIN")));
        verify(authorityRepository, times(1)).findByAuthority("ADMIN");
        verify(authorityRepository, times(1)).findByAuthority("USER");
    }

}
