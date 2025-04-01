package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.course.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    void deleteByStudentIdAndCourseId(Long studentId, Long courseId);
}
