package com.mohand.SchoolManagmentSystem.exception.course;

public class CourseNotFoundException extends CourseException {
    public CourseNotFoundException() {
        super("Course not found");
    }
}
