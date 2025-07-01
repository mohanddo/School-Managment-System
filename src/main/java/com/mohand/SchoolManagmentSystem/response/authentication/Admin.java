package com.mohand.SchoolManagmentSystem.response.authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {
    public Admin(long id, String firstName, String lastName, String email, boolean hasProfilePic, String sasTokenForReadingProfilePic, String sasTokenForWritingProfilePic, String role) {
        super(id, firstName, lastName, email, hasProfilePic, sasTokenForReadingProfilePic, sasTokenForWritingProfilePic, role);
    }
}
