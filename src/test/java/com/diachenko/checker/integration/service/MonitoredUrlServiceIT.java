package com.diachenko.checker.integration.service;


/*  health-checker
    04.03.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.AbstractIntegrationTest;
import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.model.entity.MonitoredUrl;
import com.diachenko.checker.repository.UserRepository;
import com.diachenko.checker.service.MonitoredUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MonitoredUrlServiceIT extends AbstractIntegrationTest {

    @Autowired
    private MonitoredUrlService monitoredUrlService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        AppUser user = new AppUser();
        user.setUsername("johndoe");
        user.setPassword("pass");
        userRepository.save(user);
    }

    @Test
    void subscribeUserToUrl_shouldPersistDataInDatabase() {
        monitoredUrlService.subscribeUserToUrl("johndoe", "google.com");

        AppUser updatedUser =
                userRepository.findByUsernameWithUrls("johndoe").orElseThrow();

        assertEquals(1, updatedUser.getMonitoredUrls().size());

        MonitoredUrl savedUrl =
                updatedUser.getMonitoredUrls().iterator().next();

        assertEquals("https://google.com", savedUrl.getUrl());
    }

    @Test
    void unsubscribeUserToUrl_shouldRemoveDataInDatabase() {
        monitoredUrlService.subscribeUserToUrl("johndoe", "google.com");
        AppUser userBefore =
                userRepository.findByUsernameWithUrls("johndoe").orElseThrow();
        Long urlId = userBefore.getMonitoredUrls().iterator().next().getId();

        monitoredUrlService.unsubscribeUserToUrl("johndoe", urlId);

        AppUser updatedUser =
                userRepository.findByUsernameWithUrls("johndoe").orElseThrow();

        assertEquals(0, updatedUser.getMonitoredUrls().size());
    }
}