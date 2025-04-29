package com.mohand.SchoolManagmentSystem.response.authentication;

import com.mohand.SchoolManagmentSystem.response.course.Course;
import com.mohand.SchoolManagmentSystem.response.course.StudentCourse;
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

    private List<StudentCourse> courses;
}
