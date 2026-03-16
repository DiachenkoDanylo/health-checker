package com.diachenko.checker.service;


/*  health-checker
    24.02.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.model.entity.Authority;
import com.diachenko.checker.model.entity.MonitoredUrl;
import com.diachenko.checker.model.payload.RegisterUserPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Profile("!prod")
public class DataInitializer {

    private final AuthenticationService authenticationService;
    private final AuthorityService authorityService;
    private final MonitoredUrlService monitoredUrlService;
    private final AppUserService appUserService;

    @EventListener
    public void on(ApplicationReadyEvent event) {
        initializeAuthorities();
        initializeUsers();
        initializeUrls();
        initializeAssignUrlsToUser();
    }

    public void initializeAuthorities() {
        authorityService.save(new Authority("USER"));
        authorityService.save(new Authority("ADMIN"));
    }

    public void initializeUsers() {
        authenticationService.registerUser(new RegisterUserPayload("user1", "pass1", Set.of("USER")));
        authenticationService.registerUser(new RegisterUserPayload("admin1", "admin1", Set.of("ADMIN")));
    }

    public void initializeUrls() {
        //done by flyway migration
    }

    public void initializeAssignUrlsToUser() {
        AppUser user = appUserService.getByUsernameWithUrls("user1");
        Set<MonitoredUrl> urls = monitoredUrlService.getAllUrls();
        user.getMonitoredUrls().addAll(urls);
        appUserService.saveAppUser(user);
    }
}
