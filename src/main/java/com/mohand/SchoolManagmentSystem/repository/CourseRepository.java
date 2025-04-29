package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByIdAndTeacherId(Long courseId, Long teacherId);
    List<Course> findAllByTeacherId(Long teacherId);
    Optional<Course> findByIdAndTeacherId(Long courseId, Long teacherId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Student s JOIN s.courses c " +
            "WHERE s.id = :studentId AND c.id = :courseId")
    boolean isStudentEnrolledInCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
    List<Course> findAllCoursesByStudentId(@Param("studentId") Long studentId);

    int countStudentsById(Long courseId);
}
