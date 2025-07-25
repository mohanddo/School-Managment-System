package com.mohand.SchoolManagmentSystem.repository;

import com.mohand.SchoolManagmentSystem.model.chapter.Chapter;
import com.mohand.SchoolManagmentSystem.model.chapter.Resource;
import com.mohand.SchoolManagmentSystem.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    @Query("""
    SELECT r FROM Resource r\s
    WHERE r.id = :resourceId\s
    AND r.chapter.id = :chapterId\s
    AND r.chapter.course.id = :courseId\s
""")
    Optional<Resource> findByIdAndChapterIdAndCourseId(@Param("resourceId") Long resourceId,
                                                                @Param("chapterId") Long chapterId,
                                                                @Param("courseId") Long courseId);

    @Query("""
    SELECT r FROM Resource r\s
    WHERE r.id = :resourceId\s
    AND r.chapter.id = :chapterId\s
    AND r.chapter.course.id = :courseId\s
    And r.chapter.course.teacher.id = :teacherId\s
""")
    Optional<Resource> findByIdAndChapterIdAndCourseIdAndTeacherId(@Param("resourceId") Long resourceId,
                                                       @Param("chapterId") Long chapterId,
                                                       @Param("courseId") Long courseId,
                                                                 @Param("teacherId") Long teacherId);

    @Query("select count(r) from Resource r where r.chapter.course.id = :courseId")
    int countByCourseId(Long courseId);

    @Query("SELECT MAX(r.position) FROM Resource r WHERE r.chapter.id = :chapterId AND r.chapter.course.id = :courseId")
    Integer findMaxPositionByChapter(@Param("courseId") Long courseID, @Param("chapterId") Long chapterId);

    @Query("""
    SELECT r FROM Resource r\s
    WHERE r.chapter = :chapter\s
    AND r.chapter.course = :course\s
    And r.position > :position
    ORDER BY r.position ASC
""")
    List<Resource> findByCourseAndChapterAndPositionGreaterThanOrderByPositionAsc(@Param("course") Course course,
                                                                                  @Param("chapter") Chapter chapter,
                                                                                  @Param("position") Integer position);

    List<Resource> findAllByChapterId(Long chapterId);

    @Query("""
    SELECT r.chapter.course.id
    FROM Resource r
    WHERE r.id = :resourceId
""")
    Long findCourseId(@Param("resourceId") Long resourceId);
}
