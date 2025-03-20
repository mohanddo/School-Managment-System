package com.mohand.SchoolManagmentSystem.service.course;

import com.mohand.SchoolManagmentSystem.model.Course;
import com.mohand.SchoolManagmentSystem.request.course.CreateCourseRequest;

import java.time.LocalDate;
import java.util.List;

public interface ICourseService {

    Course createCourse(CreateCourseRequest request);
    void deleteCourse(Long courseId);

    Course updateCourseTitle(Long courseId, String newTitle);
    Course updateCourseDescription(Long courseId, String newDescription);
    Course updatePrice(Long courseId, double price);
    Course updateDiscountPercentage(Long courseId, int discountPercentage);
    Course updateDiscountExpirationTime(Long courseId, LocalDate discountExpirationDate);

    List<Course> readAllCourses();
}
