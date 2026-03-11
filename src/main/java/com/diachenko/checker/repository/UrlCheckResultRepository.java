package com.diachenko.checker.repository;


/*  health-checker
    11.03.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.UrlCheckResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlCheckResultRepository extends JpaRepository<UrlCheckResult, Long> {

    List<UrlCheckResult> findAllByMonitoredUrl_Id(Long monitoredUrlId);
}
