package com.diachenko.checker.controller;


/*  health-checker
    19.02.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.payload.LoginUserPayload;
import com.diachenko.checker.model.payload.RegisterUserPayload;
import com.diachenko.checker.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginUserPayload());
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerPayload", new RegisterUserPayload());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewClient(Model model, @ModelAttribute RegisterUserPayload registerUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        model.addAttribute("loginRequest", new LoginUserPayload());
        authenticationService.registerUser(registerUserDto);
        return "login";
    }

}
