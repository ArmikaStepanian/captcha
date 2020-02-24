package com.stepanian.captcha;

import com.github.cage.Cage;
import com.stepanian.captcha.services.CreateCaptchaImageService;
import com.stepanian.captcha.util.TextGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureRestDocs("target/generated-snippets")
@AutoConfigureMockMvc
@Slf4j
public class CaptchaDocumentation {

    @MockBean
    private CreateCaptchaImageService createCaptchaImageService;
    @InjectMocks
    private Cage cage;
    @InjectMocks
    private TextGenerator textGenerator;
    @Autowired
    private MockMvc mockMvc;

    Snippet requestParameters = RequestDocumentation.requestParameters(
            parameterWithName("value")
                    .description("Any string 4-8 symbols length. Parameter is required."));


    @BeforeAll
    public static void createDirectory() {
        File file = new File("target/generated-docs/images");
        if (file.mkdirs()) {
            log.info("Directory is created");
        } else {
            log.info("Directory is not created");
        }
    }

    @Test
    public void testCreateCustomCaptchaImage() throws Exception {
        String value = "qwer12ty";
        when(createCaptchaImageService.getCaptcha(any())).thenReturn(mockCaptchaImage(value));

        MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders
                .get("http://localhost:8080/api/getImage?value={value}", value))
                .andExpect(status().isOk())
                .andDo(document("{method-name}", requestParameters))
                .andReturn();
        byte[] imageInByte = result.getResponse().getContentAsByteArray();
        try (InputStream in = new ByteArrayInputStream(imageInByte)) {
            BufferedImage bImageFromConvert = ImageIO.read(in);
            ImageIO.write(bImageFromConvert, "jpg", new File("target/generated-docs/images/custom.jpg"));
        }
    }

    @Test
    public void testCreateRandomCaptchaImage() throws Exception {
        when(createCaptchaImageService.getCaptcha()).thenReturn(mockCaptchaImage());

        MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders
                .get("http://localhost:8080/api/getImage/generateValue"))
                .andExpect(status().isOk())
                .andDo(document("{method-name}"))
                .andReturn();
        byte[] imageInByte = result.getResponse().getContentAsByteArray();
        try (InputStream in = new ByteArrayInputStream(imageInByte)) {
            BufferedImage bImageFromConvert = ImageIO.read(in);
            ImageIO.write(bImageFromConvert, "jpg", new File("target/generated-docs/images/random.jpg"));
        }
    }

    @Test
    public void testCreateCustomCaptchaImageWithoutValue() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("http://localhost:8080/api/getImage"))
                .andExpect(status().isBadRequest())
                .andDo(document("{method-name}"));
    }

    @Test
    public void testCreateCustomCaptchaImageWrongLengthValue() throws Exception {
        String value = "qwerty123";
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("http://localhost:8080/api/getImage?value={value}", value))
                .andExpect(status().isBadRequest())
                .andDo(document("{method-name}"));
    }


    private byte[] mockCaptchaImage(String value) {
        return cage.draw(value);
    }

    private byte[] mockCaptchaImage() {
        return cage.draw(RandomStringUtils.random(6, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
    }
}
