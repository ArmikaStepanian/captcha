package com.stepanian.captcha.services.impl;

import com.github.cage.Cage;
import com.stepanian.captcha.services.CreateCaptchaImageService;
import com.stepanian.captcha.util.TextGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateCaptchaImageServiceImpl implements CreateCaptchaImageService {

    private final Cage cage;
    private final TextGenerator textGenerator;

    @Override
    public byte[] getCaptcha(String value) {
        return cage.draw(value);
    }

    @Override
    public byte[] getCaptcha() {
        return cage.draw(textGenerator.generateHash());
    }
}
