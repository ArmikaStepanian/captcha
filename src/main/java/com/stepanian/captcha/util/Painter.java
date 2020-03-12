package com.stepanian.captcha.util;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

@Component
public class Painter {

    private Color backgroundColor = Color.white;
    private Color fontColor = Color.black;
    private Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();

    public byte[] draw(String value, int width, int height) throws IOException {
        float fontSize = (float) (height / 2.5);
        int padding = width / (value.length() == 0 ? 1 : value.length());

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics graphics2D = image.createGraphics();
        graphics2D.setColor(backgroundColor);
        graphics2D.fillRect(0, 0, width, height);

        for (int i = 0; i < value.length(); i++) {
            int x = (i * padding) + padding / 2;
            int y = height / 2 + new Random().nextInt(height / 4);
            drawLetter(graphics2D, chooseRandomFont(fonts), fontColor, fontSize, value.substring(i, i + 1), x, y);
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

    private void drawLetter(Graphics graphics2D, Font font, Color fontColor, float fontSize, String letter, int x, int y) {
        font = font.deriveFont(fontSize);
        graphics2D.setFont(font);
        graphics2D.setColor(fontColor);
        graphics2D.drawString(letter, x, y);
    }

    private Font chooseRandomFont(Font[] fonts) {
        int index = new Random().nextInt(fonts.length);
        return fonts[index];
    }
}