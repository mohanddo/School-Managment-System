package com.mohand.SchoolManagmentSystem.response.teacher;

import com.azure.core.annotation.Get;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class TeacherPreview {
    private long id;
    private String firstName;
    private String lastName;
    private int numberOfCourses;
    private int numberOfStudents;
    private String profilePicDownloadUrl;
    private String description;
    private String youtubeLink;
    private String facebookLink;
    private String instagramLink;

}
