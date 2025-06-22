package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.course.CurrentResource;
import com.mohand.SchoolManagmentSystem.model.course.CurrentResourceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrentResourceRepository extends JpaRepository<CurrentResource, CurrentResourceId> {

    CurrentResource findByStudentIdAndCourseId(Long studentId, Long courseId);

    Optional<CurrentResource> findByStudentIdAndResourceIdAndCourseId(Long studentId, Long resourceId, Long courseId);
}
