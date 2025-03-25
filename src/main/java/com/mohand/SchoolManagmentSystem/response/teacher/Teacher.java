package com.mohand.SchoolManagmentSystem.response.teacher;

import com.mohand.SchoolManagmentSystem.response.course.CoursePreview;

import java.util.List;
import java.util.Optional;

public record Teacher(long id,
                      String firstName,
                      String lastName,
                      Optional<String> facebookLink,
                      Optional<String> youtubeLink,
                      Optional<String> instagramLink,
                      Optional<String> description,
                      Optional<String> profilePicDownloadUrl,
                      List<CoursePreview> courses
                      ) {
}
