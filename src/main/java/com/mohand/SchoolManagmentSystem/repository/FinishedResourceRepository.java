package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.chapter.FinishedResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinishedResourceRepository extends JpaRepository<FinishedResource, Long> {
    boolean existsByResourceIdAndStudentId(Long resourceId, Long studentId);
    void deleteByResourceIdAndStudentId(Long resourceId, Long studentId);
}
