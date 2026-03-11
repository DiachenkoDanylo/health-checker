package com.diachenko.checker.controller;


/*  health-checker
    11.03.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.UrlCheckResult;
import com.diachenko.checker.repository.UrlCheckResultRepository;
import com.diachenko.checker.service.MonitoredUrlService;
import com.diachenko.checker.service.UrlCheckResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class UrlCheckResultController {

    private final UrlCheckResultService urlCheckResultService;
    private final MonitoredUrlService monitoredUrlService;

    @GetMapping("/{id}")
    public String getHistoryById(@PathVariable (name = "id") Long id, Model model) {
        List<UrlCheckResult> urlCheckResultList = urlCheckResultService.getAllUrlCheckResultsByUrlId(id);
        model.addAttribute("monitoredUrl", monitoredUrlService.getById(id));
        model.addAttribute("historyCheksList", urlCheckResultList);
        return "history";
    }
}
