package com.diachenko.checker;


/*  health-checker
    02.03.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.model.entity.Authority;
import com.diachenko.checker.model.entity.MonitoredUrl;
import com.diachenko.checker.model.entity.UrlCheckResult;
import com.diachenko.checker.model.payload.RegisterUserPayload;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TestDataFactory {

    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2026, 03, 03, 03, 33);

    private TestDataFactory() {
    }

    public static Authority userAuthority() {
        return new Authority(1L, "USER");
    }

    public static Authority adminAuthority() {
        return new Authority(2L, "ADMIN");
    }

    public static AppUser user() {
        return AppUser.builder()
                .id(10L)
                .username("johndoe")
                .password("pass")
                .authorities(Set.of(userAuthority()))
                .monitoredUrls(new HashSet<>())
                .build();
    }

    public static AppUser admin() {
        return AppUser.builder()
                .id(11L)
                .username("admin")
                .password("adminpass")
                .authorities(Set.of(adminAuthority()))
                .build();
    }

    public static RegisterUserPayload userPayload() {
        return new RegisterUserPayload("johndoe", "pass", Set.of("USER"));
    }

    public static RegisterUserPayload adminPayload() {
        return new RegisterUserPayload("doeJohn", "pass2", Set.of("ADMIN"));
    }

    public static String getRawUrl() {
        return "google.com";
    }

    public static String getNormUrl() {
        return "https://google.com";
    }

    public static MonitoredUrl monitoredUrl() {
        MonitoredUrl monitoredUrl = new MonitoredUrl();
        monitoredUrl.setId(55L);
        monitoredUrl.setUrl(getNormUrl());
        monitoredUrl.setLastUpdate(LOCAL_DATE_TIME);
        return monitoredUrl;
    }


    public static AppUser getAppUserWithUrl() {
        MonitoredUrl monitoredUrl = new MonitoredUrl();
        monitoredUrl.setId(66L);
        monitoredUrl.setUrl(getNormUrl());
        monitoredUrl.setLastUpdate(LOCAL_DATE_TIME);

        AppUser appUser = AppUser.builder()
                .id(10L)
                .username("johndoe")
                .password("pass")
                .authorities(Set.of(userAuthority()))
                .monitoredUrls(Set.of(monitoredUrl))
                .build();

        monitoredUrl.setSubscribers(Set.of(appUser));
        return appUser;
    }

    public static List<UrlCheckResult> getUrlCheckResultList() {
        UrlCheckResult urlCheckResult = UrlCheckResult.builder()
                .id(11L)
                .isUp(true)
                .responseTimeMs(200L)
                .monitoredUrl(getAppUserWithUrl().getMonitoredUrls().iterator().next())
                .httpStatus(200)
                .checkedAt(LOCAL_DATE_TIME.minus(Duration.ofMinutes(1L)))
                .build();
        UrlCheckResult urlCheckResult2 = UrlCheckResult.builder()
                .id(12L)
                .isUp(false)
                .responseTimeMs(5000L)
                .monitoredUrl(getAppUserWithUrl().getMonitoredUrls().iterator().next())
                .httpStatus(405)
                .checkedAt(LOCAL_DATE_TIME)
                .build();

        return List.of(urlCheckResult, urlCheckResult2);
    }

}
