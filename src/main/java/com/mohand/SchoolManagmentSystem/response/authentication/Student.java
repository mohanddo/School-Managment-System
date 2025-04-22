package com.mohand.SchoolManagmentSystem.response.authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Student extends User {

    public Student(String firstName, String lastName, String email, String profilePicDownloadUrl) {
        super(firstName, lastName, email, profilePicDownloadUrl);
    }
}
