package com.stepanian.captcha.web;

import com.stepanian.captcha.exceptions.IncorrectLengthException;
import com.stepanian.captcha.services.CreateCaptchaImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final CreateCaptchaImageService createCaptchaImageService;

    @RequestMapping(value = "/api/getImage", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getCaptcha(@RequestParam String value) throws IncorrectLengthException {
        if (value.length() < 4 || value.length() > 8) {
            throw new IncorrectLengthException("Value: size must be between 4 and 8");
        }
        return createCaptchaImageService.getCaptcha(value);
    }

    @RequestMapping(value = "/api/getImage/generateValue", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getCaptcha() {
        return createCaptchaImageService.getCaptcha();
    }

    @RequestMapping(value = "/api/getImage/resizable", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getCaptcha(@RequestParam String value,
                      @RequestParam(required = false) Integer width,
                      @RequestParam(required = false) Integer height) throws IOException, IncorrectLengthException {
        if (value.length() < 4 || value.length() > 8) {
            throw new IncorrectLengthException("Value: size must be between 4 and 8");
        }
        return createCaptchaImageService.getCaptcha(value, width, height);
    }
}
