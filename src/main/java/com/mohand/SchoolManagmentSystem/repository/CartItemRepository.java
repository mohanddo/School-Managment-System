package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.course.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    void deleteByStudentIdAndCourseId(Long studentId, Long courseId);
    List<CartItem> findAllByStudentId(Long studentId);
}
