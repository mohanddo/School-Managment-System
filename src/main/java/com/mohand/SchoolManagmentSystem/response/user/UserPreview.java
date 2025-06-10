package com.mohand.SchoolManagmentSystem.response.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPreview {
    protected Long id;
    protected String firstName;
    protected String lastName;
    protected Boolean hasProfilePic;
    protected String sasTokenForReadingProfilePic;
}
