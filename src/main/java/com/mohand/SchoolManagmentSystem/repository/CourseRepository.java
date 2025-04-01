package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByIdAndTeacherId(Long courseId, Long teacherId);
    List<Course> findAllByTeacherId(Long teacherId);
    Optional<Course> findByIdAndTeacherId(Long courseId, Long teacherId);
}
