package com.mohand.SchoolManagmentSystem.exception.courseReview;

public class CourseReviewNotFoundException extends CourseReviewException {
    public CourseReviewNotFoundException(String courseId, String studentId) {

        super("Student with id : " + studentId + " did not review course with id : " + courseId);
    }
}
