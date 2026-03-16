package com.diachenko.checker.service;


/*  health-checker
    16.03.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.MonitoredUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Profile({"prod", "standalone"})
public class SchedulingService {

    private final HttpService httpService;
    private final MonitoredUrlService monitoredUrlService;
    private final ExecutorService monitoringExecutor;

    @Scheduled(fixedDelay = 60000)
    public void updateStatuses() {
        Set<MonitoredUrl> urls = monitoredUrlService.getAllUrls();
        urls.forEach(url ->
                monitoringExecutor.submit(() ->
                        httpService.checkUrl(url.getId(), url.getUrl()))
        );
    }
}
