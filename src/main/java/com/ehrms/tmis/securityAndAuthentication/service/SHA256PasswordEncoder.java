package com.ehrms.tmis.securityAndAuthentication.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        System.out.println("SHA256PasswordEncoder: Encoding password");
        return hashWithSHA256(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        System.out.println("SHA256PasswordEncoder: Checking password match");
        String hashedPassword = (hashWithSHA256(rawPassword.toString())).toUpperCase();
        System.out.println("SHA256PasswordEncoder: Password hashed: " + hashedPassword);
        boolean matches = hashedPassword.equals(encodedPassword);
        System.out.println("SHA256PasswordEncoder: Encoded Password: " + encodedPassword);
        System.out.println("SHA256PasswordEncoder: Password match result = " + matches);
        return matches;
    }

    private String hashWithSHA256(String input) {
        System.out.println("SHA256PasswordEncoder: Hashing password with SHA-256");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            System.out.println("SHA256PasswordEncoder: Hashed password = " + hexString.toString());
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password with SHA-256", e);
        }
    }

}
