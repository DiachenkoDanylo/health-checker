package com.diachenko.checker.service;


/*  health-checker
    24.02.2026
    @author DiachenkoDanylo
*/


import com.diachenko.checker.model.entity.MonitoredUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class HttpService {

    private final HttpClient httpClientDefault;
    private final MonitoredUrlService monitoredUrlService;
    private final ExecutorService monitoringExecutor;
    private final UrlCheckResultService urlCheckResultService;

    @Scheduled(fixedDelay = 60000)
    public void updateStatuses() {
        System.out.println("Updating statuses");

        Set<MonitoredUrl> urls = monitoredUrlService.getAllUrls();

        System.out.println("URLs count: " + urls.size());
        System.out.println("Executor class: " + monitoringExecutor.getClass());

        urls.forEach(url ->
                monitoringExecutor.submit(() ->
                        checkUrl(url.getId(), url.getUrl()))
        );
        System.out.println("DONE UPDATING STATUSES");
    }

    private void checkUrl(Long id, String urlString) {

        System.out.println("Checking URL: " + urlString);
        System.out.println("Thread: " + Thread.currentThread());

        boolean isUp = false;
        int statusCode = 0;
        long responseTimeMs = 0;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            long start = System.nanoTime();

            HttpResponse<Void> response =
                    httpClientDefault.send(request, HttpResponse.BodyHandlers.discarding());

            long end = System.nanoTime();
            responseTimeMs = (end - start) / 1_000_000;

            statusCode = response.statusCode();
            System.out.println("Status code: " + response.statusCode());


            isUp = statusCode >= 200 &&
                    statusCode < 400;

//            monitoredUrlService.updateStatus(id, isUp);

        } catch (Exception e) {
            e.printStackTrace(); // VERY IMPORTANT
            isUp = false;
//            monitoredUrlService.updateStatus(id, false);
        } finally {
            urlCheckResultService.saveUrlCheckResult(id,isUp,statusCode,responseTimeMs);
            monitoredUrlService.updateStatus(id, isUp);
        }
    }

}
