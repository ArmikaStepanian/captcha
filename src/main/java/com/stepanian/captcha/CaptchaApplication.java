package com.stepanian.captcha;

import com.github.cage.Cage;
import com.github.cage.GCage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CaptchaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaptchaApplication.class, args);
    }

    @Bean
    public Cage cage() {
        return new GCage();
    }
}
