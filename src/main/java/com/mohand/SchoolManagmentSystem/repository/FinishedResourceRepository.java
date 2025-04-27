package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.chapter.FinishedResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FinishedResourceRepository extends JpaRepository<FinishedResource, Long> {
    boolean existsByResourceIdAndStudentId(Long resourceId, Long studentId);
    void deleteByResourceIdAndStudentId(Long resourceId, Long studentId);

    @Query("SELECT COUNT(f) FROM FinishedResource f WHERE f.resource.chapter.course.id = :courseId AND f.student.id = :studentId")
    int countFinishedResourceByCourseIdAndStudentId(Long courseId, Long studentId);
}
