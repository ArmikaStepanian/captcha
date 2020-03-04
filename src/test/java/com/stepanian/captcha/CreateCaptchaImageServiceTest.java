package com.stepanian.captcha;

import com.github.cage.Cage;
import com.stepanian.captcha.services.impl.CreateCaptchaImageServiceImpl;
import com.stepanian.captcha.util.TextGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCaptchaImageServiceTest {

    @Mock
    private Cage cage;
    @Mock
    private TextGenerator textGenerator;
    @InjectMocks
    private CreateCaptchaImageServiceImpl createCaptchaImageServiceImpl;

    @Test
    public void testRandomCaptchaNotNull() {
        when(textGenerator.generateHash()).thenReturn("hash");
        when(cage.draw(anyString())).thenReturn(new byte[1]);
        assertNotNull(createCaptchaImageServiceImpl.getCaptcha());
    }

    @Test
    public void testCustomCaptchaNotNull() {
        when(cage.draw(anyString())).thenReturn(new byte[1]);
        assertNotNull(createCaptchaImageServiceImpl.getCaptcha("1234"));
    }
}

