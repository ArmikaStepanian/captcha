package com.stepanian.captcha.services;

public interface CreateCaptchaImageService {

    byte[] getCaptcha(String value);

    byte[] getCaptcha();
}
