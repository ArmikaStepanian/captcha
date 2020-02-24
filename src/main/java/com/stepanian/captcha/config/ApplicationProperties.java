package com.stepanian.captcha.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "captcha")
@Getter
@Setter
@Validated
@Component
public class ApplicationProperties {

    @NotNull
    private int length;
    @NotNull
    private String allowedChars;
}
