package com.diachenko.checker.model.entity;


/*  health-checker
    11.03.2026
    @author DiachenkoDanylo
*/

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_check_result")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UrlCheckResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id")
    private MonitoredUrl monitoredUrl;

    private boolean isUp;

    private Integer httpStatus;

    private Long responseTimeMs;

    private LocalDateTime checkedAt;


    public UrlCheckResult() {
        // Builder usage
    }
}

