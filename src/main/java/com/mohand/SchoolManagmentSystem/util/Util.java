package com.mohand.SchoolManagmentSystem.util;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class Util {
    public static boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    private static final Pattern FACEBOOK_PATTERN = Pattern.compile("^(https?://)?(www\\.)?facebook\\.com/.*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern YOUTUBE_PATTERN = Pattern.compile("^(https?://)?(www\\.)?(youtube\\.com/|youtu\\.be/).*$", Pattern.CASE_INSENSITIVE);
    private static final Pattern INSTAGRAM_PATTERN = Pattern.compile("^(https?://)?(www\\.)?instagram\\.com/.*$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidFacebookLink(String url) {
        return url != null && FACEBOOK_PATTERN.matcher(url).matches();
    }

    public static boolean isValidYoutubeLink(String url) {
        return url != null && YOUTUBE_PATTERN.matcher(url).matches();
    }

    public static boolean isValidInstagramLink(String url) {
        return url != null && INSTAGRAM_PATTERN.matcher(url).matches();
    }

    public static String getRequestBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read request body", e);
        }
        return stringBuilder.toString();
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
