package com.mohand.SchoolManagmentSystem.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterStudentRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
