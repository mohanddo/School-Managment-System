package com.mohand.SchoolManagmentSystem.response.course;

import com.mohand.SchoolManagmentSystem.enums.Review;
import com.mohand.SchoolManagmentSystem.response.user.StudentPreview;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CourseReview {
    private Long id;
    private Review review;
    private String comment;
    private LocalDateTime dateOfCreation;
    private StudentPreview student;
}
