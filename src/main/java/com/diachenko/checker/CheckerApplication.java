package com.diachenko.checker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CheckerApplication {

    static void main(String[] args) {
        SpringApplication.run(CheckerApplication.class, args);
    }

}
