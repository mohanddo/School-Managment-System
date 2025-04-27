package com.mohand.SchoolManagmentSystem.response.authentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Student extends User {

    public Student(long id, String firstName, String lastName, String email, String profilePicDownloadUrl) {
        super(id, firstName, lastName, email, profilePicDownloadUrl);
    }

    private List<com.mohand.SchoolManagmentSystem.response.course.CoursePreview> courses;
}
