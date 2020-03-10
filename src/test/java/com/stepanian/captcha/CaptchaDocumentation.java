package com.stepanian.captcha;

import com.stepanian.captcha.services.CreateCaptchaImageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureRestDocs("target/generated-snippets")
@TestPropertySource(properties = {
        "captcha.length=6",
        "captcha.allowedChars=0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
})
@AutoConfigureMockMvc
@Slf4j
public class CaptchaDocumentation {

    @Autowired
    private CreateCaptchaImageService createCaptchaImageService;
    @Autowired
    private MockMvc mockMvc;
    @Value("${captcha.length}")
    private int length;
    @Value("${captcha.allowedChars}")
    private String allowedChars;

    Snippet requestParameters = RequestDocumentation.requestParameters(
            parameterWithName("value")
                    .description("Any string 4-8 symbols length. Parameter is required."));

    Snippet parametersForResizableImage = RequestDocumentation.requestParameters(
            parameterWithName("value")
                    .description("Any string 4-8 symbols length. Parameter is required."),
            parameterWithName("width")
                    .description("Image width. Parameter is not required. Default value = 180."),
            parameterWithName("height")
                    .description("Image height. Parameter is not required. Default value = 50.")
    );


    @BeforeAll
    public static void createDirectory() throws IOException {
        Files.createDirectories(Paths.get("target/generated-docs/images"));
    }

    @Test
    public void testCreateCustomCaptchaImage() throws Exception {
        String value = "qwer12ty";
        MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders
                .get("http://localhost:8080/api/getImage?value={value}", value))
                .andExpect(status().isOk())
                .andDo(document("{method-name}", requestParameters))
                .andReturn();
        byte[] imageInByte = result.getResponse().getContentAsByteArray();
        try (InputStream in = new ByteArrayInputStream(imageInByte)) {
            BufferedImage bImageFromConvert = ImageIO.read(in);
            ImageIO.write(bImageFromConvert, "jpg", Paths.get("target/generated-docs/images/custom.jpg").toFile());
        }
    }

    @Test
    public void testCreateRandomCaptchaImage() throws Exception {
        MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders
                .get("http://localhost:8080/api/getImage/generateValue"))
                .andExpect(status().isOk())
                .andDo(document("{method-name}"))
                .andReturn();
        byte[] imageInByte = result.getResponse().getContentAsByteArray();
        try (InputStream in = new ByteArrayInputStream(imageInByte)) {
            BufferedImage bImageFromConvert = ImageIO.read(in);
            ImageIO.write(bImageFromConvert, "jpg", Paths.get("target/generated-docs/images/random.jpg").toFile());
        }
    }

    @Test
    public void testCreateResizableCaptchaImage() throws Exception {
        String value = "12F59";
        MvcResult result = mockMvc.perform(RestDocumentationRequestBuilders
                .get("http://localhost:8080/api/getImage/resizable?value={value}&width={width}&height={height}",
                        value, 120, 30))
                .andExpect(status().isOk())
                .andDo(document("{method-name}", parametersForResizableImage))
                .andReturn();
        byte[] imageInByte = result.getResponse().getContentAsByteArray();
        try (InputStream in = new ByteArrayInputStream(imageInByte)) {
            BufferedImage bImageFromConvert = ImageIO.read(in);
            ImageIO.write(bImageFromConvert, "jpg", Paths.get("target/generated-docs/images/resizable.jpg").toFile());
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
}
