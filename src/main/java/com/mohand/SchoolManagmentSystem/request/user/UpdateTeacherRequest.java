package com.mohand.SchoolManagmentSystem.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTeacherRequest {

    @NotBlank(message = "Request must have a first name")
    private String firstName;

    @NotBlank(message = "Request must have a last name")
    private String lastName;

    @NotNull
    private boolean hasProfilePic;


    private String description;

    private String facebookLink;

    private String instagramLink;

    private String youtubeLink;
}
