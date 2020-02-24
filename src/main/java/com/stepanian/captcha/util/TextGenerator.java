package com.stepanian.captcha.util;

import com.stepanian.captcha.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TextGenerator {

    private final ApplicationProperties applicationProperties;

    public String generateHash() {
        return RandomStringUtils.random(applicationProperties.getLength(), applicationProperties.getAllowedChars());
    }
}
