package com.mohand.SchoolManagmentSystem.response.authentication;


import com.mohand.SchoolManagmentSystem.response.course.TeacherCourse;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Teacher extends User {

//    public Teacher(long id, String firstName, String lastName, String email, String profilePicDownloadUrl,
//                   int numberOfStudents, int numberOfCourses, String facebookLink, String youtubeLink, String instagramLink, String description, String sasToken) {
//        super(id, firstName, lastName, email, profilePicDownloadUrl);
//        this.numberOfStudents = numberOfStudents;
//        this.numberOfCourses = numberOfCourses;
//        this.facebookLink = facebookLink;
//        this.youtubeLink = youtubeLink;
//        this.instagramLink = instagramLink;
//        this.description = description;
//        this.sasToken = sasToken;
//    }

    private Integer numberOfStudents;
    private Integer numberOfCourses;
    private String facebookLink;
    private String youtubeLink;
    private String instagramLink;
    private String description;
    private String sasToken;
    private String baseUrl;
    private List<TeacherCourse> courses;
}
