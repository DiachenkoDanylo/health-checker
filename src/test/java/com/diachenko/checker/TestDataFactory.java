package com.diachenko.checker;


/*  health-checker
    02.03.2026
    @author DiachenkoDanylo
*/

import com.diachenko.checker.model.entity.AppUser;
import com.diachenko.checker.model.entity.Authority;
import com.diachenko.checker.model.entity.MonitoredUrl;
import com.diachenko.checker.model.payload.RegisterUserPayload;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public final class TestDataFactory {

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
        monitoredUrl.setLastUpdate(LocalDateTime.of(2026, 03, 03, 03, 33));
        return monitoredUrl;
    }


    public static AppUser getAppUserWithUrl() {
        MonitoredUrl monitoredUrl = new MonitoredUrl();
        monitoredUrl.setId(66L);
        monitoredUrl.setUrl(getNormUrl());
        monitoredUrl.setLastUpdate(LocalDateTime.of(2026, 03, 03, 03, 33));

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

}
