package com.mohand.SchoolManagmentSystem.response.teacher;

import java.util.Optional;

public record TeacherPreview(String firstName,
                             String lastName,
                             int numberOfCourses,
                             int numberOfStudents,
                             Optional<String> profilePicDownloadUrl,
                             Optional<String> description
) {
}
