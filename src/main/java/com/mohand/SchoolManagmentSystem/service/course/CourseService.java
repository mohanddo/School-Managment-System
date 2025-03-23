package com.mohand.SchoolManagmentSystem.service.course;

import com.mohand.SchoolManagmentSystem.exception.course.CourseNotFoundException;
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
    public Course create(CreateCourseRequest request) {
        Teacher teacher = teacherService.readById(request.teacherId());
        Course course = Course.builder(request.title(), request.description(), request.price(), request.pricingModel(), teacher)
                .build();
        return courseRepository.save(course);
    }
    @Override
    public void delete(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    @Override
    public Course updateTitle(Long courseId, String newTitle) {
        Course course = getById(courseId);
        course.setTitle(newTitle);
        return courseRepository.save(course);
    }

    @Override
    public Course updateDescription(Long courseId, String newDescription) {
        Course course = getById(courseId);
        course.setDescription(newDescription);
        return courseRepository.save(course);
    }

    @Override
    public Course updatePrice(Long courseId, double price) {
        Course course = getById(courseId);
        course.setPrice(price);
        return courseRepository.save(course);
    }

    @Override
    public Course updateDiscountPercentage(Long courseId, int discountPercentage) {
        Course course = getById(courseId);
        course.setDiscountPercentage(discountPercentage);
        return courseRepository.save(course);
    }

    @Override
    public Course updateDiscountExpirationTime(Long courseId, LocalDate discountExpirationDate) {
        Course course = getById(courseId);
        course.setDiscountExpirationDate(discountExpirationDate);
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course getById(Long id) {
        return courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
    }
}
