package com.diachenko.checker.service;


/*  health-checker
    24.02.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.Authority;
import com.diachenko.checker.model.payload.RegisterUserPayload;
import com.diachenko.checker.repository.AuthorityRepository;
import com.diachenko.checker.repository.MonitoredUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Profile("h2")
public class Starter {

    private final AuthenticationService authenticationService;
    private final AuthorityRepository authorityRepository;
    private final MonitoredUrlRepository monitoredUrlRepository;
    private final MonitoredUrlService monitoredUrlService;

    @EventListener
    public void on(ApplicationReadyEvent event) {
        authorityRepository.save(new Authority("USER"));
        authorityRepository.save(new Authority("ADMIN"));
        authenticationService.registerUser(new RegisterUserPayload("user1", "pass1", Set.of("USER")));
        authenticationService.registerUser(new RegisterUserPayload("admin1", "admin1", Set.of("ADMIN")));
    }
}
