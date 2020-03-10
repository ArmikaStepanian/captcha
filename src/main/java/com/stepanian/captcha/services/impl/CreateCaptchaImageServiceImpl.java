package com.stepanian.captcha.services.impl;

import com.github.cage.Cage;
import com.stepanian.captcha.config.ApplicationProperties;
import com.stepanian.captcha.services.CreateCaptchaImageService;
import com.stepanian.captcha.util.Painter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class CreateCaptchaImageServiceImpl implements CreateCaptchaImageService {

    private final ApplicationProperties applicationProperties;
    private final Cage cage;
    private final Painter painter;

    @Override
    public byte[] getCaptcha(String value) {
        return cage.draw(value);
    }

    @Override
    public byte[] getCaptcha() {
        String hash = RandomStringUtils
                .random(applicationProperties.getValueLength(), applicationProperties.getAllowedChars());
        return cage.draw(hash);
    }

    @Override
    public byte[] getCaptcha(String value, Integer width, Integer height) throws IOException {
        if (width == null) {
            width = applicationProperties.getWidth();
        }
        if (height == null) {
            height = applicationProperties.getHeight();
        }
        return painter.draw(value, width, height);
    }
}
