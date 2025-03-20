package com.mohand.SchoolManagmentSystem.request.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
