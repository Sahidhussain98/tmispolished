package com.tmisehrms.user.controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import jakarta.servlet.ServletOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.code.kaptcha.impl.DefaultKaptcha;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class CaptchaController {

     @Autowired
    private DefaultKaptcha captchaProducer;

    // Endpoint to generate and serve the CAPTCHA image
    @GetMapping("/captcha-image")
    public void getCaptchaImage(HttpSession session, HttpServletResponse response) throws Exception {
        response.setContentType("image/png");

        // Generate CAPTCHA text
        String captchaText = captchaProducer.createText();

        // Store the CAPTCHA text in the session for later validation
        session.setAttribute("captcha", captchaText);

        // Create CAPTCHA image
        BufferedImage captchaImage = captchaProducer.createImage(captchaText);

        // Write the image to the response output stream
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(captchaImage,"png", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }
    
}
