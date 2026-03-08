package com.diachenko.checker.config;


/*  health-checker
    19.02.2026
    @author DiachenkoDanylo
*/

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class Config {

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ExecutorService monitoringExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean(name = "httpClientDefault")
    public HttpClient httpClient() {
        return HttpClient.newBuilder().build();
    }
}
