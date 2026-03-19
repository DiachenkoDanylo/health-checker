package com.diachenko.checker.controller;


import com.diachenko.checker.model.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*  health-checker
    19.03.2026
    @author DiachenkoDanylo
*/

@RestController
@RequestMapping("/k8s")
@RequiredArgsConstructor
public class KubernetesController {

    @GetMapping("/test-info")
    public ResponseEntity<String> k8s(@AuthenticationPrincipal AppUser principal) {
        String response = "User ID: " + principal.getId() + "\n" +
                "Username: " + principal.getUsername() + "\n" +
                "Pod name: " + System.getenv("HOSTNAME") + "\n" +
                "Port: " + System.getenv("SERVER_PORT");
        return ResponseEntity.ok(response);
    }
}
