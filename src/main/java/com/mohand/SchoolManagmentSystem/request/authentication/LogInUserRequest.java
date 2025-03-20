package com.mohand.SchoolManagmentSystem.request.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInUserRequest {
    private String email;
    private String password;
}
