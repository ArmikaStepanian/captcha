package com.stepanian.captcha.services;

import java.io.IOException;

public interface CreateCaptchaImageService {

    byte[] getCaptcha(String value);

    byte[] getCaptcha();

    byte[] getCaptcha(String value, Integer width, Integer height) throws IOException;
}
