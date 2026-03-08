package com.diachenko.checker.unit.service;


/*  health-checker
    28.02.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.TestDataFactory;
import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.repository.UserRepository;
import com.diachenko.checker.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AppUserServiceTest {

    @InjectMocks
    private AppUserService appUserService;

    @Mock
    private UserRepository userRepository;

    private final AppUser validAppUser = TestDataFactory.user();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_success() {
        Mockito.when(userRepository.findById(validAppUser.getId())).thenReturn(Optional.of(validAppUser));

        assertEquals(validAppUser, appUserService.getById(validAppUser.getId()));
    }

    @Test
    void getById_exception_nullableId() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            appUserService.getById(null);
        });

        assertEquals("User with id : " + null + " not found", thrown.getMessage());
        verify(userRepository, times(1)).findById(null);
    }

    @Test
    void getById_exception_notFound() {
        Mockito.when(userRepository.findById(validAppUser.getId())).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            appUserService.getById(validAppUser.getId());
        });

        assertEquals("User with id : " + validAppUser.getId() + " not found", thrown.getMessage());
        verify(userRepository, times(1)).findById(validAppUser.getId());
    }

    @Test
    void getByUsername_success() {
        Mockito.when(userRepository.findByUsername("USER")).thenReturn(Optional.of(validAppUser));

        assertEquals(validAppUser, appUserService.getByUsername("USER"));
    }

    @Test
    void getByUsername_exception_nullableId() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            appUserService.getByUsername(null);
        });

        assertEquals("User with username : " + null + " not found", thrown.getMessage());
        verify(userRepository, times(1)).findByUsername(null);
    }

    @Test
    void getByUsername_exception_notFound() {
        Mockito.when(userRepository.findByUsername("USER")).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            appUserService.getByUsername("USER");
        });

        assertEquals("User with username : " + "USER" + " not found", thrown.getMessage());
        verify(userRepository, times(1)).findByUsername("USER");
    }

    @Test
    void deleteAppUserItself_success() {
        Mockito.when(userRepository.findById(validAppUser.getId())).thenReturn(Optional.of(validAppUser));
        appUserService.deleteAppUserItself(validAppUser);

        verify(userRepository, times(1)).deleteById(validAppUser.getId());
        verify(userRepository, times(1)).findById(validAppUser.getId());
    }

    @Test
    void deleteById_exception_notFound() {
        Mockito.when(userRepository.findById(validAppUser.getId())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            appUserService.deleteAppUserItself(validAppUser);
        });

        assertEquals("User with id : " + validAppUser.getId() + " not found", thrown.getMessage());
        verify(userRepository, times(1)).findById(validAppUser.getId());
    }

}
