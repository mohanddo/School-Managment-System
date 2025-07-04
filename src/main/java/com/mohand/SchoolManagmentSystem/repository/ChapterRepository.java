package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.chapter.Chapter;
import com.mohand.SchoolManagmentSystem.model.chapter.Video;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import com.mohand.SchoolManagmentSystem.model.user.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Optional<Chapter> findByIdAndCourseId(Long id, Long courseId);
    boolean existsByIdAndCourseId(Long id, Long courseId);

    @Query("SELECT MAX(c.position) FROM Chapter c WHERE c.course = :course")
    Integer findMaxPositionByCourse(@Param("course") Course course);


    @Query("""
    SELECT c FROM Chapter c\s
    WHERE c.id = :chapterId\s
    AND c.course.id = :courseId\s
    AND c.course.teacher.id = :teacherId
""")
    Optional<Chapter> findByIdAndCourseIdAndTeacherId(@Param("chapterId") Long chapterId,
                                                                     @Param("courseId") Long courseId,
                                                                     @Param("teacherId") Long teacherId);

    List<Chapter> findByCourseAndPositionGreaterThanOrderByPositionAsc(Course course, Integer position);

    @Query("""

            SELECT c\s
FROM Chapter c\s
WHERE c.course.id = :courseId\s
  AND c.course.teacher = :teacherId\s
  AND c.position <= :position\s
ORDER BY c.position ASC
""")
    List<Chapter> findByCourseIdAndTeacherIdAndPositionLessOrEqualThanOrderByPositionAsc(@Param("courseId") Long courseId,@Param("teacherId") Long teacherId, Integer position);

    @Query("""

            SELECT c\s
FROM Chapter c\s
WHERE c.course.id = :courseId\s
  AND c.course.teacher = :teacherId\s
  AND c.position >= :position\s
ORDER BY c.position ASC
""")
    List<Chapter> findByCourseIdAndTeacherIdAndPositionGreaterOrEqualThanOrderByPositionAsc(@Param("courseId") Long courseId,@Param("teacherId") Long teacherId, Integer position);

}
