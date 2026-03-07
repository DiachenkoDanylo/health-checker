package com.diachenko.checker.repository;


/*  health-checker
    24.02.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.MonitoredUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MonitoredUrlRepository extends JpaRepository<MonitoredUrl, Long> {
    Optional<MonitoredUrl> findByUrl(String url);

    Set<MonitoredUrl> findAllBy();
}
