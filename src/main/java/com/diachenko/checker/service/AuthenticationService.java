package com.diachenko.checker.service;


/*  health-checker
    20.02.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.model.entity.Authority;
import com.diachenko.checker.model.payload.RegisterUserPayload;
import com.diachenko.checker.repository.AuthorityRepository;
import com.diachenko.checker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public AppUser registerUser(RegisterUserPayload payload) {
        try {
            userRepository.findByUsername(payload.getUsername())
                    .ifPresent(user -> {
                        throw new RuntimeException("USER DUPLICATE");
                    });

            Set<Authority> authorities = fetchAuthorities(payload.getAuthorities());

            AppUser appUser = AppUser.builder()
                    .username(payload.getUsername())
                    .password(passwordEncoder.encode(payload.getPassword()))
                    .authorities(authorities)
                    .build();
            log.info("USER REGISTERED {}", appUser);
            return userRepository.save(appUser);
        } catch (RuntimeException e) {
            return null;
        }

    }


    public Set<Authority> fetchAuthorities(Set<String> authority) {
        Set<Authority> authorities = new HashSet<>();
        for (String a : authority) {
            authorityRepository.findByAuthority(a).ifPresentOrElse(authorities::add, () -> {
                log.debug("AUTHORITY NOT FOUND {}", a);
            });
        }
        return !authorities.isEmpty() ? authorities : Set.of(authorityRepository.findByAuthority("USER").get());
    }

}
