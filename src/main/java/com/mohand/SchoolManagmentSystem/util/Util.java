package com.mohand.SchoolManagmentSystem.util;

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
}
