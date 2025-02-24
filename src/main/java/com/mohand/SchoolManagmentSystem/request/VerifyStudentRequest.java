package com.mohand.SchoolManagmentSystem.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyStudentRequest {
    private String email;
    private String verificationCode;
}
