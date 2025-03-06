package com.mohand.SchoolManagmentSystem.util;

import java.util.regex.Pattern;

public class Util {
    public static boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }
}
