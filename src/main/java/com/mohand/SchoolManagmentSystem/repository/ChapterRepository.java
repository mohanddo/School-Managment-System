package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.chapter.Chapter;
import com.mohand.SchoolManagmentSystem.model.chapter.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Optional<Chapter> findByIdAndCourseId(Long id, Long courseId);
    boolean existsByIdAndCourseId(Long id, Long courseId);


    @Query("""
    SELECT c FROM Chapter c\s
    WHERE c.id = :chapterId\s
    AND c.course.id = :courseId\s
    AND c.course.teacher.id = :teacherId
""")
    Optional<Chapter> findByIdAndCourseIdAndTeacherId(@Param("chapterId") Long chapterId,
                                                                     @Param("courseId") Long courseId,
                                                                     @Param("teacherId") Long teacherId);
}
