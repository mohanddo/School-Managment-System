package com.mohand.SchoolManagmentSystem.response.authentication;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Teacher extends User {

    public Teacher(String firstName, String lastName, String email, String profilePicDownloadUrl, String jwtToken,
                   int numberOfStudents, int numberOfCourses, String facebookLink, String youtubeLink, String instagramLink, String description, String sasToken) {
        super(firstName, lastName, email, profilePicDownloadUrl, jwtToken);
        this.numberOfStudents = numberOfStudents;
        this.numberOfCourses = numberOfCourses;
        this.facebookLink = facebookLink;
        this.youtubeLink = youtubeLink;
        this.instagramLink = instagramLink;
        this.description = description;
        this.sasToken = sasToken;
    }

    private Integer numberOfStudents;
    private Integer numberOfCourses;
    private String facebookLink;
    private String youtubeLink;
    private String instagramLink;
    private String description;
    private String sasToken;
    private String baseUrl;
}
