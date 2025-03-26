package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.course.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    Optional<CourseReview> findByStudentIdAndCourseId(Long studentId, Long courseId);
    void deleteByStudentIdAndCourseId(Long studentId, Long courseId);
}
