package com.diachenko.checker.model.entity;


/*  health-checker
    24.02.2026
    @author DiachenkoDanylo
*/

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "url",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "url")
        }
)
@Getter
@Setter
public class MonitoredUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToMany(mappedBy = "monitoredUrls", fetch = FetchType.LAZY)
    private Set<AppUser> subscribers = new HashSet<>();

    private boolean isUp = false;

    private Long failureCount = 0L;

    private LocalDateTime lastUpdate = LocalDateTime.now();

}
