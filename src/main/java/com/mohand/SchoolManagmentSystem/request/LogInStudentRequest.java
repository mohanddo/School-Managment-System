package com.mohand.SchoolManagmentSystem.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInStudentRequest {
    private String email;
    private String password;
}
