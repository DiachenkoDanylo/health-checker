package com.diachenko.checker.service;


/*  health-checker
    24.02.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AppUserService {

    private final UserRepository userRepository;

    public AppUser getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id : " + id + " not found"));
    }

    public AppUser getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User with username : " + username + " not found"));
    }

    public AppUser getByIdWithUrls(Long id) {
        return userRepository.findByIdWithUrls(id).orElseThrow(() -> new RuntimeException("User with id : " + id + " not found"));
    }

    public AppUser getByUsernameWithUrls(String username) {
        return userRepository.findByUsernameWithUrls(username).orElseThrow(() -> new RuntimeException("User with username : " + username + " not found"));
    }

    @Transactional
    public void deleteAppUserItself(AppUser appUser) {
        if (appUser == getById(appUser.getId())) {
            userRepository.deleteById(appUser.getId());
        }
    }
}
