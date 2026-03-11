package com.diachenko.checker.service;


/*  health-checker
    11.03.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.UrlCheckResult;
import com.diachenko.checker.repository.UrlCheckResultRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UrlCheckResultService {

    private final UrlCheckResultRepository urlCheckResultRepository;
    private final MonitoredUrlService monitoredUrlService;

    @Transactional
    public void saveUrlCheckResult(Long id, boolean isUp, Integer status, Long responseTimeMs) {
        UrlCheckResult urlCheckResult = UrlCheckResult.builder()
                .isUp(isUp)
                .monitoredUrl(monitoredUrlService.getById(id))
                .httpStatus(status)
                .responseTimeMs(responseTimeMs)
                .checkedAt(LocalDateTime.now())
                .build();
        urlCheckResultRepository.save(urlCheckResult);
    }

    public List<UrlCheckResult> getAllUrlCheckResultsByUrlId(Long urlId) {
        return urlCheckResultRepository.findAllByMonitoredUrl_Id(urlId);
    }
}
