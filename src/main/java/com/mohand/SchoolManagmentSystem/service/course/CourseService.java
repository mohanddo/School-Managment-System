package com.mohand.SchoolManagmentSystem.service.course;

import com.mohand.SchoolManagmentSystem.model.Course;
import com.mohand.SchoolManagmentSystem.model.Teacher;
import com.mohand.SchoolManagmentSystem.repository.CourseRepository;
import com.mohand.SchoolManagmentSystem.repository.TeacherRepository;
import com.mohand.SchoolManagmentSystem.request.course.CreateCourseRequest;
import com.mohand.SchoolManagmentSystem.service.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {

    private final CourseRepository courseRepository;
    private final TeacherService teacherService;

    @Override
    public Course createCourse(CreateCourseRequest request) {
//        Teacher teacher = teacherService.readTeacherById(request.teacherId());
//        Course course = Course.builder(request.title(), request.description(), request.price(), request.pricingModel(), teacher)
//                .;
        return null;
    }

    @Override
    public void deleteCourse(Long courseId) {

    }

    @Override
    public Course updateCourseTitle(Long courseId, String newTitle) {
        return null;
    }

    @Override
    public Course updateCourseDescription(Long courseId, String newDescription) {
        return null;
    }

    @Override
    public Course updatePrice(Long courseId, double price) {
        return null;
    }

    @Override
    public Course updateDiscountPercentage(Long courseId, int discountPercentage) {
        return null;
    }

    @Override
    public Course updateDiscountExpirationTime(Long courseId, LocalDate discountExpirationDate) {
        return null;
    }

    @Override
    public List<Course> readAllCourses() {
        return List.of();
    }
}
