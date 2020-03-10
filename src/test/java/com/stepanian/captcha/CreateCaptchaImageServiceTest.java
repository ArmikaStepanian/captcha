package com.stepanian.captcha;

import com.github.cage.Cage;
import com.stepanian.captcha.config.ApplicationProperties;
import com.stepanian.captcha.services.impl.CreateCaptchaImageServiceImpl;
import com.stepanian.captcha.util.Painter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCaptchaImageServiceTest {

    @Mock
    private ApplicationProperties applicationProperties;
    @Mock
    private Cage cage;
    @Mock
    private Painter painter;
    @InjectMocks
    private CreateCaptchaImageServiceImpl createCaptchaImageServiceImpl;

    @Test
    public void testRandomCaptchaNotNull() {
        when(cage.draw(anyString())).thenReturn(new byte[1]);
        assertNotNull(createCaptchaImageServiceImpl.getCaptcha());
    }

    @Test
    public void testCustomCaptchaNotNull() {
        when(cage.draw(anyString())).thenReturn(new byte[1]);
        assertNotNull(createCaptchaImageServiceImpl.getCaptcha("1234"));
    }

    @Test
    public void testResizableCaptchaNotNull() throws IOException {
        when(painter.draw(anyString(), anyInt(), anyInt())).thenReturn(new byte[1]);
        assertNotNull(createCaptchaImageServiceImpl.getCaptcha("1234", 120, 30));
        assertNotNull(createCaptchaImageServiceImpl.getCaptcha("1234", null, null));
    }
}

