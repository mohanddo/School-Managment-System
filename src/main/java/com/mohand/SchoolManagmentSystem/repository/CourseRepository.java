package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByIdAndTeacherId(Long courseId, Long teacherId);
    List<Course> findAllByTeacherId(Long teacherId);
}
