package com.mohand.SchoolManagmentSystem.service.course;

import com.mohand.SchoolManagmentSystem.model.Course;
import com.mohand.SchoolManagmentSystem.request.course.CreateCourseRequest;

import java.time.LocalDate;
import java.util.List;

public interface ICourseService {

    Course create(CreateCourseRequest request);
    void delete(Long courseId);

    Course updateTitle(Long courseId, String newTitle);
    Course updateDescription(Long courseId, String newDescription);
    Course updatePrice(Long courseId, double price);
    Course updateDiscountPercentage(Long courseId, int discountPercentage);
    Course updateDiscountExpirationTime(Long courseId, LocalDate discountExpirationDate);

    List<Course> getAll();
    Course getById(Long id);
}
