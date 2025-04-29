package com.mohand.SchoolManagmentSystem.response.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherPreview extends UserPreview {
    private int numberOfCourses;
    private int numberOfStudents;
    private String description;
    private String youtubeLink;
    private String facebookLink;
    private String instagramLink;
}
