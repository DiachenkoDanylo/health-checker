package com.diachenko.checker.service;


import com.diachenko.checker.model.entity.Authority;
import com.diachenko.checker.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*  health-checker
    16.03.2026
    @author DiachenkoDanylo
*/

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public Optional<Authority> save(Authority authority) {
        try {
            findByAuthority(authority.getAuthority());
            return Optional.of(authorityRepository.save(authority));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Authority findByAuthority(String authority) {
        return authorityRepository.findByAuthority(authority).orElseThrow(() -> new RuntimeException("Authority not found!"));
    }
}
