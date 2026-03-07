package com.diachenko.checker.controller;


import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.model.entity.MonitoredUrl;
import com.diachenko.checker.service.MonitoredUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/*  health-checker
    19.02.2026
    @author DiachenkoDanylo
*/

@Controller
@RequestMapping("/api/")
@RequiredArgsConstructor
public class HealthCheckController {

    private final MonitoredUrlService monitoredUrlService;

    @GetMapping("")
    public String healthCheck(Model model, @AuthenticationPrincipal AppUser principal) {
        Set<MonitoredUrl> urls = monitoredUrlService.getUserUrlsByUsername(principal.getUsername());
        model.addAttribute("urls", urls);
        return "health";
    }

    @PostMapping("add")
    public String addMonitoredUrl(Model model, @ModelAttribute MonitoredUrl monitoredUrl, @AuthenticationPrincipal AppUser principal) {
        monitoredUrlService.subscribeUserToUrl(principal.getUsername(), monitoredUrl.getUrl());
        return "redirect:/api/";
    }

    @PostMapping("delete/{id}")
    public String deleteMonitoredUrl(Model model, @PathVariable String id, @AuthenticationPrincipal AppUser principal) {
        monitoredUrlService.unsubscribeUserToUrl(principal.getUsername(), Long.parseLong(id));
        return "redirect:/api/";
    }


}
