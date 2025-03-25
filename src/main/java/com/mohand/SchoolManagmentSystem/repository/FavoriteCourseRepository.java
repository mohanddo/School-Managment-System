package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.course.FavoriteCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteCourseRepository extends JpaRepository<FavoriteCourse, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    void deleteByStudentIdAndCourseId(Long studentId, Long courseId);
}
