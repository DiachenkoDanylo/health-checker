package com.diachenko.checker.service;


/*  health-checker
    24.02.2026
    @author DiachenkoDanylo
*/


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class HttpService {

    private final HttpClient httpClientDefault;
    private final MonitoredUrlService monitoredUrlService;
    private final UrlCheckResultService urlCheckResultService;

    public void checkUrl(Long id, String urlString) {
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

            isUp = statusCode >= 200 &&
                    statusCode < 400;

        } catch (Exception e) {
            isUp = false;
            statusCode = 418;
        } finally {
            urlCheckResultService.saveUrlCheckResult(id, isUp, statusCode, responseTimeMs);
            monitoredUrlService.updateStatus(id, isUp);
        }
    }
}
