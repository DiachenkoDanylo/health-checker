package com.diachenko.checker.config.security;


/*  health-checker
    19.03.2026
    @author DiachenkoDanylo
*/

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
@Profile({"prod", "standalone"})
public class RedisSessionConfig {

}
