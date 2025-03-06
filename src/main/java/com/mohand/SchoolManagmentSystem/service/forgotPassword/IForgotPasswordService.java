package com.mohand.SchoolManagmentSystem.service.forgotPassword;

public interface IForgotPasswordService {
    void verifyEmail(String email);
    void verifyOTP(String email, Integer otp);
}
