package com.diachenko.checker.integration;


/*  health-checker
    04.03.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.model.payload.RegisterUserPayload;
import com.diachenko.checker.repository.AuthorityRepository;
import com.diachenko.checker.repository.UserRepository;
import com.diachenko.checker.service.AppUserService;
import com.diachenko.checker.service.AuthenticationService;
import com.diachenko.checker.service.MonitoredUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Profile("test")
@RequiredArgsConstructor
public class IntegrationTestDataFactory {

    private final AuthenticationService authenticationService;
    private final AuthorityRepository authorityRepository;
    private final MonitoredUrlService monitoredUrlService;
    private final UserRepository userRepository;
    private final AppUserService appUserService;

    public void createDefaultUser() {
//        authorityRepository.save(new Authority("USER"));
//        authorityRepository.save(new Authority("ADMIN"));

        authenticationService.registerUser(
                new RegisterUserPayload("johndoe", "pass1", Set.of("USER"))
        );
    }

    public void createDefaultUrl() {
        monitoredUrlService.subscribeUserToUrl("johndoe", "google.com");
    }

    public AppUser getDefaultUser() {
        return appUserService.getByUsername("johndoe");
    }

}
