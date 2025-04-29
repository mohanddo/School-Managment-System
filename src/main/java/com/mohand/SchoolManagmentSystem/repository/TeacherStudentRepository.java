package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.course.TeacherStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherStudentRepository extends JpaRepository<TeacherStudent, Long> {
    boolean existsByStudentIdAndTeacherId(Long studentId, Long teacherId);

    int countByTeacherId(Long teacherId);
    void deleteByStudentIdAndTeacherId(Long studentId, Long teacherId);
}
