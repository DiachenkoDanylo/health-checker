package com.diachenko.checker.unit.service;


/*  health-checker
    24.02.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.TestDataFactory;
import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.model.entity.MonitoredUrl;
import com.diachenko.checker.repository.MonitoredUrlRepository;
import com.diachenko.checker.repository.UserRepository;
import com.diachenko.checker.service.AppUserService;
import com.diachenko.checker.service.MonitoredUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MonitoredUrlServiceTest {

    private final String rawUrl = TestDataFactory.getRawUrl();
    private final String normUrl = TestDataFactory.getNormUrl();
    private final AppUser appUser = TestDataFactory.user();
    private final MonitoredUrl monitoredUrl = TestDataFactory.monitoredUrl();
    @InjectMocks
    private MonitoredUrlService monitoredUrlService;
    @Mock
    private MonitoredUrlRepository urlRepository;
    @Mock
    private AppUserService appUserService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void subscribeUserToUrl_success_newUrl() {
        when(appUserService.getByUsername(appUser.getUsername())).thenReturn(appUser);
        when(urlRepository.findByUrl(normUrl)).thenReturn(Optional.empty());
        when(urlRepository.save(any())).thenReturn(monitoredUrl);

        monitoredUrlService.subscribeUserToUrl("johndoe", "google.com");

        verify(urlRepository, times(1)).save(any());
        assertEquals(1, appUser.getMonitoredUrls().size());
    }

    @Test
    void subscribeUserToUrl_success_existingUrl() {
        when(appUserService.getByUsername(appUser.getUsername())).thenReturn(appUser);
        when(urlRepository.findByUrl(normUrl)).thenReturn(Optional.of(monitoredUrl));

        monitoredUrlService.subscribeUserToUrl("johndoe", "google.com");

        verify(urlRepository, times(0)).save(any());
        assertEquals(1, appUser.getMonitoredUrls().size());
    }

    @Test
    void unsubscribeUserToUrl_success_existingUrl() {
        appUser.getMonitoredUrls().add(monitoredUrl);
        when(appUserService.getByUsername(appUser.getUsername())).thenReturn(appUser);
        when(urlRepository.findById(monitoredUrl.getId())).thenReturn(Optional.of(monitoredUrl));

        monitoredUrlService.unsubscribeUserToUrl("johndoe", monitoredUrl.getId());

        verify(userRepository, times(1)).save(any());
        assertEquals(0, appUser.getMonitoredUrls().size());
    }

    @Test
    void unsubscribeUserToUrl_exception_nonExistingUrl() {
        when(appUserService.getByUsername(appUser.getUsername())).thenReturn(appUser);
        when(urlRepository.findById(monitoredUrl.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            monitoredUrlService.unsubscribeUserToUrl("johndoe", 1L);
        });

        assertEquals("MONITORED URL NOT FOUND", exception.getMessage());
    }

    @Test
    void normalizeUrl_success_HighCase() {
        assertEquals("https://google.com", monitoredUrlService.normalizeUrl("gOOgle.Com"));
    }

    @Test
    void normalizeUrl_success_LowCase() {
        assertEquals("https://google.com", monitoredUrlService.normalizeUrl("google.com"));

    }

    @Test
    void normalizeUrl_success_http() {
        assertEquals("http://google.com", monitoredUrlService.normalizeUrl("http://googlE.coM"));
    }

    @Test
    void getUserUrlsByUsername_success() {
        appUser.getMonitoredUrls().add(monitoredUrl);
        when(appUserService.getByUsername(appUser.getUsername())).thenReturn(appUser);

        assertEquals(appUser.getMonitoredUrls(), monitoredUrlService.getUserUrlsByUsername("johndoe"));
    }

    @Test
    void getUserUrlsByUsername_exception_notFound() {
        when(appUserService.getByUsername("johndoe")).thenThrow(new RuntimeException("User with username : " + appUser.getUsername() + " not found"));

        Exception thrown = assertThrows(RuntimeException.class, () -> {
            monitoredUrlService.getUserUrlsByUsername("johndoe");
        });

        assertEquals("User with username : " + appUser.getUsername() + " not found", thrown.getMessage());
    }

    @Test
    void updateStatus_success() {
        LocalDateTime start = TestDataFactory.monitoredUrl().getLastUpdate();
        when(urlRepository.findById(monitoredUrl.getId())).thenReturn(Optional.of(monitoredUrl));

        monitoredUrlService.updateStatus(monitoredUrl.getId(), true);

        verify(urlRepository, times(1)).save(any());
        assertTrue(monitoredUrl.isUp());
        assertTrue(start.isBefore(monitoredUrl.getLastUpdate()));
    }

    @Test
    void updateStatus_success_noChanged() {
        LocalDateTime start = TestDataFactory.monitoredUrl().getLastUpdate();
        when(urlRepository.findById(monitoredUrl.getId())).thenReturn(Optional.of(monitoredUrl));

        monitoredUrlService.updateStatus(monitoredUrl.getId(), false);

        assertTrue(start.isBefore(monitoredUrl.getLastUpdate()));
        assertEquals(1L, monitoredUrl.getFailureCount());
    }

    @Test
    void updateStatus_success_setUpDownAfter3Checks() {
        LocalDateTime start = TestDataFactory.monitoredUrl().getLastUpdate();
        when(urlRepository.findById(monitoredUrl.getId())).thenReturn(Optional.of(monitoredUrl));

        monitoredUrlService.updateStatus(monitoredUrl.getId(), false);
        monitoredUrlService.updateStatus(monitoredUrl.getId(), false);
        monitoredUrlService.updateStatus(monitoredUrl.getId(), false);

        assertTrue(start.isBefore(monitoredUrl.getLastUpdate()));
        assertEquals(3L, monitoredUrl.getFailureCount());
        assertFalse(monitoredUrl.isUp());
    }

    @Test
    void updateStatus_exception_notFound() {
        when(urlRepository.findById(monitoredUrl.getId())).thenReturn(Optional.empty());

        Exception thrown = assertThrows(RuntimeException.class, () -> {
            monitoredUrlService.updateStatus(monitoredUrl.getId(), true);
        });

        assertEquals("MONITORED URL NOT FOUND", thrown.getMessage());
    }

    @Test
    void getAllUrls() {
        when(urlRepository.findAllBy()).thenReturn(Set.of(monitoredUrl));

        assertEquals(Set.of(monitoredUrl), monitoredUrlService.getAllUrls());
    }

}
