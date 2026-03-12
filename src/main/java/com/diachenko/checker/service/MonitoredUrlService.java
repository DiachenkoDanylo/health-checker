package com.diachenko.checker.service;


/*  health-checker
    24.02.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.model.entity.MonitoredUrl;
import com.diachenko.checker.repository.MonitoredUrlRepository;
import com.diachenko.checker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MonitoredUrlService {

    private final MonitoredUrlRepository urlRepository;
    private final AppUserService appUserService;
    private final UserRepository userRepository;

    public MonitoredUrl getById(Long id) {
        return urlRepository.getReferenceById(id);
    }

    @Transactional
    public void subscribeUserToUrl(String username, String rawUrl) {

        String normalizedUrl = normalizeUrl(rawUrl);

        AppUser user = appUserService.getByUsername(username);

        MonitoredUrl url = urlRepository
                .findByUrl(normalizedUrl)
                .orElseGet(() -> {
                    MonitoredUrl newUrl = new MonitoredUrl();
                    newUrl.setUrl(normalizedUrl);
                    return urlRepository.save(newUrl);
                });

        user.getMonitoredUrls().add(url);
    }

    @Transactional
    public void unsubscribeUserToUrl(String username, Long monitoredUrlId) {
        AppUser user = appUserService.getByUsername(username);
        MonitoredUrl url = urlRepository
                .findById(monitoredUrlId)
                .orElseThrow(() -> new RuntimeException("MONITORED URL NOT FOUND"));
        user.getMonitoredUrls().remove(url);
        userRepository.save(user);
    }

    public String normalizeUrl(String rawUrl) {
        if (!rawUrl.startsWith("http://") && !rawUrl.startsWith("https://")) {
            return "https://" + rawUrl.toLowerCase();
        }
        return rawUrl.toLowerCase();
    }

    public Set<MonitoredUrl> getUserUrlsByUsername(String username) {
        AppUser user = appUserService.getByUsername(username);

        return user.getMonitoredUrls();
    }

    @Transactional
    public void updateStatus(Long id, boolean isUp) {
        MonitoredUrl url = urlRepository.findById(id).orElseThrow(() -> new RuntimeException("MONITORED URL NOT FOUND"));
        url.setFailureCount(isUp ? 0 : url.getFailureCount() + 1);
        if (url.getFailureCount() >= 3) {
            url.setUp(false);
        }
        url.setLastUpdate(LocalDateTime.now());
        url.setUp(isUp);
        urlRepository.save(url);
    }

    public Set<MonitoredUrl> getAllUrls() {
        return urlRepository.findAllBy();
    }
}
