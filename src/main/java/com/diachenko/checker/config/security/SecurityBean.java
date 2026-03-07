package com.diachenko.checker.config.security;


/*  health-checker
    19.02.2026
    @author DiachenkoDanylo
*/

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityBean {

    private final String[] WHITELIST = new String[]{"/health", "/h2-console/**", "/auth/**", "/access-denied", "/actuator/**"};
    private final String[] ADMIN_URLs = new String[]{"/admin"};
    private final String[] USER_URLs = new String[]{"/api/**"};

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    @Profile("standalone")
    public SecurityFilterChain securityFilterChainStandalone(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(config -> config
                        .requestMatchers(WHITELIST).permitAll()
                        .requestMatchers(ADMIN_URLs).hasRole("ADMIN")
                        .requestMatchers(USER_URLs).hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .userDetailsService(userDetailsService)
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/api/", true)
                        .failureUrl("/auth/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(config -> config
                        .requestMatchers(WHITELIST).permitAll()
                        .requestMatchers(ADMIN_URLs).hasAnyAuthority("ADMIN")
                        .requestMatchers(USER_URLs).hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .userDetailsService(userDetailsService)
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/api/", true)
                        .failureUrl("/auth/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .permitAll()
                );

        return http.build();
    }

}
